package com.mukcha.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mukcha.domain.NaverProfile;
import com.mukcha.service.UserService;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequiredArgsConstructor
public class OAuthController {

    private static final String CLIENT_ID = "29ePxl3H1XTUdqOjcD4X";
    private static final String CLIENT_SECRET = "qtu0G6tjle";

    private final UserService userService;


    @GetMapping(value = "/naver/auth")
    public String naverCallback(@RequestParam String code, @RequestParam String state) {
        // HttpHeader and HttpBody object를 naver token안에 넣음
        HttpEntity<MultiValueMap<String, String>> naverTokenRequest = generateAuthCodeRequest(code, state);
        ResponseEntity<String> response = requestAccessToken(naverTokenRequest);
        String accessToken = extractAccessToken(response.getBody());
        
        HttpEntity<MultiValueMap<String, String>> naverProfileRequest = generateProfileRequest(accessToken);
        ResponseEntity<String> naverProfileResponse = requestProfile(naverProfileRequest);
        System.out.println(">>> naverProfileResponse: "+naverProfileResponse.getBody());

        // naver user object
        ObjectMapper objectMapper = new ObjectMapper();
        NaverProfile naverProfile = null;

        try {
            naverProfile = objectMapper.readValue(naverProfileResponse.getBody(), NaverProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // 가입자인지 가입자가 아닌지 확인하여
        if (userService.findByEmail(naverProfile.getResponse().getEmail()).isPresent()) {
            // 가입자라면 Login Process
            userService.doLogin(naverProfile.getResponse().getEmail(), naverProfile.getResponse().getId());
        } else {
            // 가입자가 아니라면 회원가입 및 로그인 진행
            doSaveAndLogin(naverProfile);
        }
        return "redirect:/";
    }


    // 회원가입 및 로그인 진행
    private void doSaveAndLogin(NaverProfile naverProfile) {
        userService.saveNaverUser(
            naverProfile.getResponse().getEmail(),
            naverProfile.getResponse().getId(),
            naverProfile.getResponse().getNickname(),
            naverProfile.getResponse().getProfile_image(),
            naverProfile.getResponse().getGender(),
            naverProfile.getResponse().getBirthyear(),
            naverProfile.getResponse().getBirthday()
        );
        log.info(">>> 회원<"+naverProfile.getResponse().getEmail()+">님의 네이버 회원가입을 진행합니다.");
        userService.doLogin(naverProfile.getResponse().getEmail(), naverProfile.getResponse().getId());
    }




    private ResponseEntity<String> requestProfile(HttpEntity<MultiValueMap<String, String>> request) {
        // POST 방식으로 네이버에게 REQUEST
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.POST,
                request,
                String.class
        );
    }


    private HttpEntity<MultiValueMap<String, String>> generateProfileRequest(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+ accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        return new HttpEntity<>(headers);
    }


    private String extractAccessToken(String accessTokenResponse) {
        JSONObject jsonObject = new JSONObject(accessTokenResponse);
        return jsonObject.getString("access_token");
    }



    private ResponseEntity<String> requestAccessToken(HttpEntity<MultiValueMap<String, String>> request) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                request,
                String.class
        );
    }


    private HttpEntity<MultiValueMap<String, String>> generateAuthCodeRequest(String code, String state) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", CLIENT_ID);
        params.add("client_secret", CLIENT_SECRET);
        params.add("code", code);
        return new HttpEntity<>(params, headers);
    }


}
/*
https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=29ePxl3H1XTUdqOjcD4X&state=STATE_STRING&redirect_uri=http://localhost:8000/naver/auth
*/