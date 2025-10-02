package cc.aliko.payment_service.service.impl;

import cc.aliko.payment_service.config.SipayProperties;
import cc.aliko.payment_service.dto.DirectPaymentRequest;
import cc.aliko.payment_service.exception.PaymentProcessingException;
import cc.aliko.payment_service.repository.TransactionRepository;
import cc.aliko.payment_service.entity.Transaction;
import cc.aliko.payment_service.service.PaymentService;
import cc.aliko.payment_service.service.TokenManager;
import cc.aliko.payment_service.util.HashUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final RestTemplate restTemplate;
    private final SipayProperties sipayProperties;
    private final TokenManager tokenManager;
    private final String defaultReturnUrl;
    private final String defaultCancelUrl;
    private final TransactionRepository transactionRepository;

    @Override
    public String directPayment(DirectPaymentRequest request) {
        // Önce token elde et
        String token = tokenManager.getToken();

        // Eğer return/cancel URL girilmemişse application.properties dosyasından çek
        String returnUrl = (request.getReturn_url() == null || request.getReturn_url().isBlank())
                ? defaultReturnUrl
                : request.getReturn_url();

        String cancelUrl = (request.getCancel_url() == null || request.getCancel_url().isBlank())
                ? defaultCancelUrl
                : request.getCancel_url();

        // Hash değeri üret
        String hashKey = HashUtil.generateHashKey(
                request.getTotal(),
                Integer.parseInt(request.getInstallments_number()),
                request.getCurrency_code(),
                sipayProperties.getMerchantKey(),
                request.getInvoice_id(),
                sipayProperties.getAppSecret()
        );

        // Form oluştur (Smart3DPay JSON formatında veri almıyor)
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("authorization", "Bearer " + token);
        formData.add("merchant_key", sipayProperties.getMerchantKey());
        formData.add("cc_holder_name", request.getCc_holder_name());
        formData.add("cc_no", request.getCc_no());
        formData.add("expiry_month", request.getExpiry_month());
        formData.add("expiry_year", request.getExpiry_year());
        formData.add("cvv", request.getCvv());
        formData.add("currency_code", request.getCurrency_code());
        formData.add("installments_number", request.getInstallments_number());
        formData.add("invoice_id", request.getInvoice_id());
        formData.add("invoice_description", request.getInvoice_description());
        formData.add("name", request.getName());
        formData.add("surname", request.getSurname());
        formData.add("total", request.getTotal());
        formData.add("items", request.getItems());
        formData.add("cancel_url", cancelUrl);
        formData.add("return_url", returnUrl);
        formData.add("hash_key", hashKey);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);

        String url = sipayProperties.getBaseUrl() + "/api/paySmart3D";

        try {
                //Form verilerini gönder
                ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

                // Veritabanına kaydet
                Transaction tx = new Transaction();
                tx.setInvoiceId(request.getInvoice_id());
                tx.setCardHolder(request.getCc_holder_name());
                tx.setCardMasked(request.getCc_no().replaceAll(".(?=.{4})", "*"));
                tx.setCurrency(request.getCurrency_code());
                tx.setAmount(request.getTotal());
                tx.setStatus("INITIATED");
                tx.setResponseMessage("Transaction sent to Sipay, awaiting 3D Secure result");

                transactionRepository.save(tx);

                // HTML döndür
                return response.getBody();
        }
        catch (Exception e) {
                throw new PaymentProcessingException("Ödeme sırasında bir hata oluştu.", e);
        }
    }
}
