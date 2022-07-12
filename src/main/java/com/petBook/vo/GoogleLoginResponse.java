package com.petBook.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoogleLoginResponse {
    @Schema(description = "액세스 토큰")
    private String accessToken; // 애플리케이션이 Google API 요청을 승인하기 위해 보내는 토큰
    @Schema(description = "토큰의 남은 시간")
    private String expiresIn; // Access Token의 남은 수명
    @Schema(description = "리프레쉬 토큰")
    private String refreshToken; // 새 엑세스 토큰을 얻는데 사용할 수 있는 토큰
    @Schema(description = "정보 동의 항목")
    private String scope;
    @Schema(description = "토큰 유형")
    private String tokenType; // 반환된 토큰 유형(Bearer 고정)
    @Schema(description = "JWT")
    private String idToken;
}
