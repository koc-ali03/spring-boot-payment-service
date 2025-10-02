package cc.aliko.payment_service.dto;

import lombok.Data;

@Data
public class TokenData {
    private String token;
    private Integer is_3d;
    private String expires_at;
}
