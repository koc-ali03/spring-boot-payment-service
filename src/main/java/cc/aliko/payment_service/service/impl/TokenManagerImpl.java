package cc.aliko.payment_service.service.impl;

import cc.aliko.payment_service.dto.TokenResponse;
import cc.aliko.payment_service.exception.TokenFetchException;
import cc.aliko.payment_service.service.SipayIntegrationService;
import cc.aliko.payment_service.service.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TokenManagerImpl implements TokenManager {

    private final SipayIntegrationService sipayIntegrationService;

    private String cachedToken;
    private Instant expiresAt;

    @Override
    public synchronized String getToken() {
        if (cachedToken == null || expiresAt == null || Instant.now().isAfter(expiresAt)) {
            TokenResponse response = sipayIntegrationService.getToken();
            if (response == null || response.getData() == null || response.getData().getToken() == null) {
                throw new TokenFetchException("Token elde edilemedi.");
            }


            cachedToken = response.getData().getToken();

            String exp = response.getData().getExpires_at();
            try {
                expiresAt = Instant.parse(exp.replace(" ", "T"));
            }
            catch (Exception e) {
                expiresAt = Instant.now().plusSeconds(3600);
            }

        }
        return cachedToken;
    }
}
