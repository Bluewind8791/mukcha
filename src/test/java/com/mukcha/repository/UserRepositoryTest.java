package com.mukcha.repository;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mukcha.domain.User;
import com.mukcha.repository.helper.WithRepositoryTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@SpringBootTest
@ActiveProfiles("local")
public class UserRepositoryTest extends WithRepositoryTest {

    @BeforeEach
    void before() {
        prepareTest();
    }


    @Test
    void testFindByEmail() {
        User foundUser = userRepository.findByEmail("test@email.com").get();
        assertEquals("test@email.com", foundUser.getEmail());
    }

    @Test
    void testFindByNickname() {
        User foundUser = userRepository.findByNickname("testUsername").get();
        assertEquals("testUsername", foundUser.getNickname());
    }
}
