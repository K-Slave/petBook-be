package com.petBook.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.petBook.config.ConfigUtils;
import com.petBook.vo.GoogleLoginRequest;
import com.petBook.vo.GoogleLoginResponse;
import com.petBook.vo.GoogleLoginVO;
import com.petBook.vo.KakaoLoginResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Controller
@Log4j2

public class GoogleController {

    private final ConfigUtils configUtils;


    public GoogleController(ConfigUtils configUtils) {
        this.configUtils = configUtils;
    }

    //    @Tag(name="소셜 로그인", description = "Google 소셜 로그인 API")
    @ApiOperation(
            value = "Google 로그인",
            notes = "Google 로그인 페이지로 이동하는 메소드"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "토큰 발급 성공",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = GoogleLoginResponse.class
                                    )))
            })
    @GetMapping(value = "/google/login")
    public ResponseEntity<GoogleLoginResponse> moveGoogleInitUrl() {
        String authUrl = configUtils.googleInitUrl();
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

    //    @Tag(name="소셜 로그인", description = "Google 소셜 로그인 API")
    @GetMapping(value = "/google/login/oauth2")
    public ResponseEntity<Object> redirectGoogleLogin(
            @RequestParam(value = "code") String authCode
    ) {
        log.info("code : " + authCode);
        //HTTP 통신을 위해 RestTemplate 활용
        RestTemplate restTemplate = new RestTemplate();
        try {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("client_id", configUtils.getGoogleClientId());
            params.add("client_secret", configUtils.getGoogleSecret());
            params.add("code", authCode);
            params.add("redirect_uri", configUtils.getGoogleRedirectUrl());
            params.add("grant_type", "authorization_code");
            // Http Header 설정
            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    configUtils.getGoogleAuthUrl() + "/token",
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            log.info("response: " + response);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            GoogleLoginResponse googleLoginResponse = objectMapper.readValue(response.getBody(),
                    new TypeReference<GoogleLoginResponse>() {
                    });

            return ResponseEntity.ok().body(googleLoginResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.badRequest().body(null);
    }
}
