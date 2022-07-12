package com.petBook.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.petBook.config.ConfigUtils;
import com.petBook.vo.NaverLoginRequest;
import com.petBook.vo.NaverLoginResponse;
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

@Controller
@Log4j2
public class NaverController {
    private final ConfigUtils configUtils;

    public NaverController(ConfigUtils configUtils) {
        this.configUtils = configUtils;
    }

    @GetMapping(value = "/naver/login")
    public ResponseEntity<Object> moveNaverInitUrl() {
        String authUrl = configUtils.naverInitUrl();
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

    @GetMapping(value = "/naver/login/oauth2")
    public ResponseEntity<Object> redirectNaverLogin(
            @RequestParam(value = "code") String authCode,
            @RequestParam(value = "state") String state
    ) {
        log.info("naverCode : " + authCode);

        RestTemplate restTemplate = new RestTemplate();
//        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(configUtils.getNaverAuthUrl())
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", configUtils.getNaverClientId())
                .queryParam("client_secret", configUtils.getNaverClientSecret())
                .queryParam("code", authCode)
                .queryParam("state", state);
        try {
            HttpHeaders headers = new HttpHeaders();

            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            log.info("response: " + response);

            return ResponseEntity.ok().body(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }

//        return ResponseEntity.badRequest().body(null);

        return ResponseEntity.ok().body(null);
    }
}
