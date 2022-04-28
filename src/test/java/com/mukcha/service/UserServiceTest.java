package com.mukcha.service;

import static org.junit.jupiter.api.Assertions.*;


import com.mukcha.domain.User;
import com.mukcha.service.helper.WithTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@SpringBootTest
@ActiveProfiles("local")
public class UserServiceTest extends WithTest {

    @BeforeEach
    protected void before() {
        prepareTest();
    }


    @Test
    @DisplayName("1. 사용자 생성 테스트")
    void test_1() {
        // create
        User user = userTestHelper.createUser("ben@user.test", "TestUser");
        // find
        User savedUser = userService.findUser(user.getUserId());
        // test
        assertEquals("ben@user.test", savedUser.getEmail());
        assertEquals("TestUser", savedUser.getNickname());
    }

    @Test
    @DisplayName("2. 이메일은 중복될 수 없다")
    void test_2() {
        userTestHelper.createUser("test1@test.com", "TestUser1");
        assertThrows(DataIntegrityViolationException.class, () -> userTestHelper.createUser("test1@test.com", "TestUser2"));
    }

}
/*
    @Test
    @DisplayName("9. 회원 탈퇴를 진행한다. (구현예정)")
    void test_9() {
        User user1 = userTestHelper.createUser("test1@user.test", "test1");
        User user2 = userTestHelper.createUser("test2@user.test", "test2");
        userService.disableUser(user1.getUserId());
        userService.disableUser(user2.getUserId());
        assertEquals(Optional.empty(), userService.findUser(user1.getUserId()));
        assertEquals(Optional.empty(), userService.findUser(user2.getUserId()));
    }
*/