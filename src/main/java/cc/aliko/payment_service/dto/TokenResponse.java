package cc.aliko.payment_service.dto;

import lombok.Data;

@Data
public class TokenResponse {
    private int status_code;
    private String status_description;
    private TokenData data;
}
