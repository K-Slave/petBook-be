package com.petBook.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NaverLoginRequest {
    private String client_id;
    private String client_secret;
    private String grant_type;
    private String code;
    private String state;
}
