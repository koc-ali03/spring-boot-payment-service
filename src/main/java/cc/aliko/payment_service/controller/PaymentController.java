package cc.aliko.payment_service.controller;

import cc.aliko.payment_service.dto.DirectPaymentRequest;
import cc.aliko.payment_service.entity.Transaction;
import cc.aliko.payment_service.repository.TransactionRepository;
import cc.aliko.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final TransactionRepository transactionRepository;

    @PostMapping(value = "/directPayment", produces = MediaType.TEXT_HTML_VALUE) // Ödeme yapma sayfasına istek
    public ResponseEntity<String> directPayment(@RequestBody DirectPaymentRequest request) {
        String html = paymentService.directPayment(request);
        return ResponseEntity.ok(html);
    }

    @GetMapping("/result") // Ödeme başarılı
    public ResponseEntity<String> handleSuccess(@RequestParam Map<String,String> params) {
        System.out.println("Ödeme başarılı: " + params);

        String invoiceId = params.get("invoice_id");
        if (invoiceId != null) {
            Optional<Transaction> txOpt = transactionRepository.findByInvoiceId(invoiceId);
            txOpt.ifPresent(tx -> {
                tx.setStatus("SUCCESS");
                String statusCode = params.getOrDefault("status_code", "N/A");
                String statusDescription = params.getOrDefault("status_description", "N/A");
                tx.setResponseMessage("status_code=" + statusCode + ", description=" + statusDescription);
                transactionRepository.save(tx);
            });
        }

        return ResponseEntity.ok("Ödeme başarılı: " + params);
    }


    @GetMapping("/cancel") // Ödeme başarısız
    public ResponseEntity<String> handleCancel(@RequestParam Map<String,String> params) {
        System.out.println("Ödeme başarısız: " + params);

        String invoiceId = params.get("invoice_id");
        if (invoiceId != null) {
            Optional<Transaction> txOpt = transactionRepository.findByInvoiceId(invoiceId);
            txOpt.ifPresent(tx -> {
                tx.setStatus("FAILED");
                String statusCode = params.getOrDefault("status_code", "N/A");
                String statusDescription = params.getOrDefault("status_description", "N/A");
                tx.setResponseMessage("status_code=" + statusCode + ", description=" + statusDescription);
                transactionRepository.save(tx);
            });
        }

        return ResponseEntity.ok("Ödeme başarısız: " + params);
    }

}
