package com.mukcha.backend.domain;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    void test() {
        User user = new User();
        user.setUsername("ben");
        user.setEmail("ben@email.com");
        user.setGender(Gender.MALE);
        user.setBirthday(LocalDate.of(1991, 12, 14));

        System.out.println(user.toString());
    }

    @DisplayName("1. User Test")
    @Test
    void test_1() {
        User user3 = User.builder()
                .username("ben")
                .email("ben@email.com")
                .gender(Gender.MALE)
                .birthday(LocalDate.of(1991, 12, 14))
                .build();
        
        System.out.println(user3.toString());
    }
}
