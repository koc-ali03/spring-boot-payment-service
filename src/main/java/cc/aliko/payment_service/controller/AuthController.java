package cc.aliko.payment_service.controller;

import cc.aliko.payment_service.service.AuthService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register") // Kayıt ol
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        authService.register(request.getPhone(), request.getPassword());
        return ResponseEntity.ok("Kayıt başarılı.");
    }

    @PostMapping("/login") // Giriş yap
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.getPhone(), request.getPassword());
        return ResponseEntity.ok(token);
    }

    @Data
    public static class RegisterRequest {
        private String phone;
        private String password;
    }

    @Data
    public static class LoginRequest {
        private String phone;
        private String password;
    }
}
