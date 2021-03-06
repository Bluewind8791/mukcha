package com.mukcha.controller;

import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProfileControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetProfile() {
        // when
        String profile = this.restTemplate.getForObject("/profile", String.class);
        System.out.println(">>> Profile: "+profile);
        // then
        Assertions.assertThat(profile).isEqualTo("test");
    }


}
