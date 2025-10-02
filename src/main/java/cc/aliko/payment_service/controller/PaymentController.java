package cc.aliko.payment_service.controller;

import cc.aliko.payment_service.dto.DirectPaymentRequest;
import cc.aliko.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping(value = "/directPayment", produces = MediaType.TEXT_HTML_VALUE) // Ödeme yapma sayfasına istek
    public ResponseEntity<String> directPayment(@RequestBody DirectPaymentRequest request) {
        String html = paymentService.directPayment(request);
        return ResponseEntity.ok(html);
    }

    @GetMapping("/return") // Ödeme başarılı
    public ResponseEntity<String> handleSuccess(@RequestParam Map<String,String> params) {
        // log or save params for debugging
        System.out.println("Ödeme başarılı: " + params);
        return ResponseEntity.ok("Ödeme başarılı: " + params);
    }

    @GetMapping("/cancel") // Ödeme başarısız
    public ResponseEntity<String> handleCancel(@RequestParam Map<String,String> params) {
        System.out.println("Ödeme başarısız: " + params);
        return ResponseEntity.ok("Ödeme başarısız: " + params);
    }
}
