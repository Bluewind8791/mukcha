package com.mukcha.backend.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.mukcha.backend.domain.Authority;
import com.mukcha.backend.domain.Gender;
import com.mukcha.backend.domain.User;
import com.mukcha.backend.service.helper.WithUserTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@SpringBootTest
public class UserServiceTest extends WithUserTest {

    @BeforeEach
    protected void before() {
        prepareUserServiceTest();
    }


    @DisplayName("1. 사용자 생성 테스트")
    @Test
    void test_1() {
        userTestHelper.createUser("ben@test.com", "ben");

        List<User> list = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        assertEquals(1, list.size());
        assertEquals("ben@test.com", list.get(0).getEmail());
        assertEquals("ben", list.get(0).getNickname());
    }

    @DisplayName("2. 닉네임은 중복될 수 없다")
    @Test
    void test_2() {
        userTestHelper.createUser("ben@test.com", "ben");
        assertThrows(DataIntegrityViolationException.class, () -> userTestHelper.createUser("ben2@test.com", "ben"));
    }

    @DisplayName("3. 이메일은 중복될 수 없다")
    @Test
    void test_3() {
        userTestHelper.createUser("ben@test.com", "ben");
        assertThrows(DataIntegrityViolationException.class, () -> userTestHelper.createUser("ben@test.com", "ben2"));
    }

    @DisplayName("4. nickname, email, profileImage, birthday, gender 수정")
    @Test
    void test_4() {
        User user = userTestHelper.createUser("ben@test.com", "ben");
        userService.updateNickname(user.getUserId(), "testname");
        userService.updateEmail(user.getUserId(), "test@test.com");
        userService.updateProfileImage(user.getUserId(), "testImage");
        userService.updateBirthday(user.getUserId(), LocalDate.of(1991, 12, 14));
        userService.updateGender(user.getUserId(), Gender.MALE);

        List<User> list = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        assertEquals("testname", list.get(0).getNickname());
        assertEquals("test@test.com", list.get(0).getEmail());
        assertEquals("testImage", list.get(0).getProfileImage());
        assertEquals(LocalDate.of(1991, 12, 14), list.get(0).getBirthday());
        assertEquals(Gender.MALE, list.get(0).getGender());
    }

    @DisplayName("5. ADMIN 권한 부여")
    @Test
    void test_5() {
        User user = userTestHelper.createUser("ben@test.com", "ben", Authority.ROLE_USER); // make user
        userService.addAuthority(user.getUserId(), Authority.ROLE_ADMIN); // add authority
        User savedUser = userService.findUser(user.getUserId()).get();
        userTestHelper.assertUser(savedUser, "ben@test.com", "ben", Authority.ROLE_USER, Authority.ROLE_ADMIN);
    }

    @DisplayName("6. ADMIN 권한 취소")
    @Test
    void test_6() {
        User user = userTestHelper.createUser("ben@test.com", "ben", Authority.ROLE_USER, Authority.ROLE_ADMIN);
        userService.removeAuthority(user.getUserId(), Authority.ROLE_ADMIN);
        User savedUser = userService.findUser(user.getUserId()).get();
        userTestHelper.assertUser(savedUser, "ben@test.com", "ben", Authority.ROLE_USER);
    }

    @DisplayName("7. UserSecurityService의 email로 검색할 수 있다")
    @Test
    void test_7() {
        userTestHelper.createUser("ben@test.com", "ben");
        User savedUser = (User) userSecurityService.loadUserByUsername("ben@test.com");
        userTestHelper.assertUser(savedUser, "ben@test.com", "ben");
    }

    @DisplayName("8. 패스워드를 수정할 수 있다.")
    @Test
    void test_8() {
        User user = userTestHelper.createUser("ben@test.com", "ben", "123456"); // make user
        userService.updatePassword(user.getUserId(), "111111"); // update password
        User savedUser = userService.findUser(user.getUserId()).get(); // find user
        assertEquals("111111", savedUser.getPassword()); // assert
    }




}
