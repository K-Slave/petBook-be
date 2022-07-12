package com.petBook.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NaverLoginResponse {
    @Schema(description = "접근 토큰, 발급 후 expires_in 파라미터에 설정된 시간(초)이 지나면 만료됨")
    private String access_token;
    @Schema(description = "갱신 토큰, 접근 토큰이 만료될 경우 토큰을 다시 발급받을 때 사용")
    private String refresh_token;
    @Schema(description = "접근 토큰의 타입으로 Bearer와 MAC의 두가지를 지원")
    private String token_type;
    @Schema(description = "접근 토큰의 유효 기간(초)")
    private int expires_in;
    @Schema(description = "에러코드")
    private String error;
    @Schema(description = "에러 메시지")
    private String error_description;
}
