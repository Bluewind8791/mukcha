package com.mukcha.backend.repository;

import java.time.LocalDate;

import com.mukcha.backend.domain.Gender;
import com.mukcha.backend.domain.User;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("1. User Repository Test")
    @Test
    void test_1() {
        User user = User.builder()
                .username("ben")
                .password("1234")
                .email("ben@email.com")
                .gender(Gender.MALE)
                .birthday(LocalDate.of(1991, 12, 14))
                .build();
        
        userRepository.save(user);

        System.out.println(userRepository.findAll());
    }
}
