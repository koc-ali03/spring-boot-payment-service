package cc.aliko.payment_service.service;

public interface AuthService {
    String login(String phone, String password);
    Long getUserIdByToken(String token);
    void register(String phone, String password);
}
