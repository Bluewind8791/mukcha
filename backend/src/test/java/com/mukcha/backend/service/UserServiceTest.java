package com.mukcha.backend.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import com.mukcha.backend.domain.Gender;
import com.mukcha.backend.domain.User;
import com.mukcha.backend.repository.UserRepository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;


    @DisplayName("1. Test Join")
    @Test
    void test_1() {
        //given
        User user = new User();
        user.setUsername("ben");
        user.setPassword("1234");
        user.setEmail("ben@email.com");
        user.setGender(Gender.MALE);
        user.setBirthday(LocalDate.of(1991, 12, 14));

        // when
        Long saveId = userService.join(user);

        // then
        User findUser = userRepository.findById(saveId).get();
        Assertions.assertThat(user.getUsername()).isEqualTo(findUser.getUsername());
    }

    @DisplayName("2. Test Duplicate Username")
    @Test
    void test_2() {
        User user1 = new User();
        user1.setUsername("test");
        user1.setPassword("1234");
        user1.setEmail("email1");
        User user2 = new User();
        user2.setUsername("test");
        user2.setPassword("1234");
        user2.setEmail("email2");

        userService.join(user1);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> userService.join(user2));

        Assertions.assertThat(ex.getMessage()).isEqualTo("이미 사용중인 아이디입니다.");
    }

    @DisplayName("3. Test Duplicate Email")
    @Test
    void test_3() {
        User user1 = new User();
        user1.setUsername("test1");
        user1.setPassword("1234");
        user1.setEmail("email");
        User user2 = new User();
        user2.setUsername("test2");
        user2.setPassword("1234");
        user2.setEmail("email");

        userService.join(user1);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> userService.join(user2));

        Assertions.assertThat(ex.getMessage()).isEqualTo("이미 사용중인 이메일입니다.");
    }


    
}
