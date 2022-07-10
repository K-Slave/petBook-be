package com.petBook.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KakaoLoginRequest {
    private String grant_type;
    private String client_id;
    private String redirect_uri;
    private String code;
}
