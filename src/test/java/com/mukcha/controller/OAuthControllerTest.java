package com.mukcha.controller;

import com.mukcha.domain.NaverClient;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
public class OAuthControllerTest {

    @Autowired
    NaverClient naverClient;

    @Test
    @DisplayName("1. application")
    void test_1() {
        System.out.println(naverClient.clientId);
        System.out.println(naverClient.clientSecret);
    }

}
