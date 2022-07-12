package com.petBook.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KakaoLoginResponse {
    @Schema(description = "사용자 액세스 토큰 값")
    private String access_token;
    @Schema(description = "토큰 타입, bearer 로 고정")
    private String token_type;
    @Schema(description = "사용자 리프레시 토큰 값")
    private String refresh_token;
    @Schema(description = "JWT 토큰, Base64 인코딩 된 사용자 인증 정보 포함")
    private String id_token;
    @Schema(description = "액세스 토큰과 ID 토큰의 만료 시간(초)")
    private int expires_in;
    @Schema(description = "인증된 사용자의 정보 조회 권한 범위, 범위가 여러 개일 경우, 공백으로 구분")
    private String scope;
    @Schema(description = "리프레시 토큰의 만료 시간(초)")
    private int refresh_token_expires_in;
}
