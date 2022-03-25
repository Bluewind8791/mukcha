package com.mukcha.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RestControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetProfile() {
        // when
        String profile = this.restTemplate.getForObject("/profile", String.class);
        System.out.println(">>> Profile: "+profile);
        // then
        // Assertions.assertThat(profile).isEqualTo("[real, real-db, oauth]");
    }


}