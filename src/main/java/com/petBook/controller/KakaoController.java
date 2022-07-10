package com.petBook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.petBook.config.ConfigUtils;
import com.petBook.vo.KakaoLoginRequest;
import io.swagger.models.Response;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Controller
@Log4j2
public class KakaoController {
    private final ConfigUtils configUtils;

    public KakaoController(ConfigUtils configUtils) {
        this.configUtils = configUtils;
    }

    @GetMapping(value = "/kakao/login")
    public ResponseEntity<Object> moveKakaoInitUrl() {
        String authUrl = configUtils.kakaoInitUrl();
        URI redirectUri = null;
        try {
            redirectUri = new URI(authUrl);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(redirectUri);
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "/kakao/login/oauth2")
    public ResponseEntity<Object> redirectKakaoLogin(
            @RequestParam(value = "code") String authCode
    ) {
        log.info("kakaoCode : " + authCode);
        RestTemplate restTemplate = new RestTemplate();
        KakaoLoginRequest requestParams = KakaoLoginRequest.builder()
                .grant_type("authorization_code")
                .client_id(configUtils.getKakaoRestKey())
                .redirect_uri(configUtils.getKakaoRedirectUrl())
                .code(authCode)
                .build();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<KakaoLoginRequest> httpRequestEntity = new HttpEntity<>(requestParams, headers);
            ResponseEntity<String> apiResponseJson = restTemplate.postForEntity(configUtils.getKakaoAuthUrl(),
                    httpRequestEntity, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        }catch (Exception e){
            e.printStackTrace();
        }

        return ResponseEntity.ok().body(authCode);
    }
}
