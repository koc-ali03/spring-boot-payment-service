package cc.aliko.payment_service.service;

import cc.aliko.payment_service.dto.TokenResponse;

public interface SipayIntegrationService {
    TokenResponse getToken();
}
