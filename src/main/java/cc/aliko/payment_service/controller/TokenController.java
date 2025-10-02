package cc.aliko.payment_service.controller;

import cc.aliko.payment_service.dto.TokenResponse;
import cc.aliko.payment_service.service.SipayIntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TokenController {

    private final SipayIntegrationService sipayIntegrationService;

    @GetMapping("/token")
    public ResponseEntity<String> generateToken() {
        TokenResponse response = sipayIntegrationService.getToken();
        System.out.println("Token API cevabı: " + response);
        return ResponseEntity.ok("OK");
    }
}
