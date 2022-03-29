package com.mukcha.domain;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Component
public class NaverClient {

    @Value("${naver-client-id}")
    public String clientId;

    @Value("${naver-client-secret}")
    public String clientSecret;

}
