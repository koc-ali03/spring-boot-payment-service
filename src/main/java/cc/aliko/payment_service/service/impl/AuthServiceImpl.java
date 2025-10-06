package cc.aliko.payment_service.service.impl;

import cc.aliko.payment_service.entity.User;
import cc.aliko.payment_service.repository.UserRepository;
import cc.aliko.payment_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final Map<String, Long> activeTokens = new HashMap<>();

    @Override
    public void register(String phone, String password) {
        Optional<User> existing = userRepository.findByPhone(phone);
        if (existing.isPresent()) {
            throw new RuntimeException("Bu telefon numarası zaten kayıtlı.");
        }

        User user = new User();
        user.setPhone(phone);
        user.setPassword(password);
        userRepository.save(user);
    }

    @Override
    public String login(String phone, String password) {
        Optional<User> userOpt = userRepository.findByPhone(phone);
        if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(password)) {
            throw new RuntimeException("Geçersiz telefon numarası veya şifre.");
        }

        String token = UUID.randomUUID().toString();
        activeTokens.put(token, userOpt.get().getId());
        return token;
    }

    @Override
    public Long getUserIdByToken(String token) {
        return activeTokens.get(token);
    }
}
