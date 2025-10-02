package cc.aliko.payment_service.service.impl;

import cc.aliko.payment_service.config.SipayProperties;
import cc.aliko.payment_service.dto.TokenRequest;
import cc.aliko.payment_service.dto.TokenResponse;
import cc.aliko.payment_service.service.SipayIntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements SipayIntegrationService {

    private final RestTemplate restTemplate;
    private final SipayProperties sipayProperties;

    @Override
    public TokenResponse getToken() {
        String url = sipayProperties.getBaseUrl() + "/api/token";

        TokenRequest request = new TokenRequest(
                sipayProperties.getAppId(),
                sipayProperties.getAppSecret()
        );

        return restTemplate.postForObject(url, request, TokenResponse.class);
    }
}
