package com.petBook.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.petBook.config.ConfigUtils;
import com.petBook.vo.GoogleLoginRequest;
import com.petBook.vo.KakaoLoginRequest;
import com.petBook.vo.KakaoLoginResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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
        try {

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", configUtils.getKakaoRestKey());
            params.add("redirect_uri", configUtils.getKakaoRedirectUrl());
            params.add("code", authCode);
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    configUtils.getKakaoAuthUrl(),
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            KakaoLoginResponse kakaoLoginResponse = objectMapper.readValue(response.getBody(),
                    new TypeReference<KakaoLoginResponse>() {
                    });

            log.info("jwt: ", kakaoLoginResponse.getId_token());
            String jwtToken = kakaoLoginResponse.getId_token();

            HttpHeaders headers1 = new HttpHeaders();
            headers1.add("Authorization", "Bearer " + kakaoLoginResponse.getAccess_token());

            HttpEntity entity1 = new HttpEntity(headers1);

            RestTemplate restTemplate1 = new RestTemplate();
            ResponseEntity<String> response1 = restTemplate1.exchange(
                    configUtils.getKakaoKapiHost() + "/v1/user/access_token_info",
                    HttpMethod.GET,
                    entity1,
                    String.class
            );
            log.info("response1: ", response1.getBody());
            Map<String, String> result = new HashMap<>();
            result.put("token", jwtToken);

            if(response != null){
                return ResponseEntity.ok().body(kakaoLoginResponse);
            }else{
                throw new Exception("Kakao OAuth failed!");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return ResponseEntity.badRequest().body(null);
    }
}
