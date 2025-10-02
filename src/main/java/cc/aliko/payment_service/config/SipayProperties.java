package cc.aliko.payment_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "sipay")
public class SipayProperties {
    private String baseUrl;
    private String appId;
    private String appSecret;
    private String merchantKey;
}
