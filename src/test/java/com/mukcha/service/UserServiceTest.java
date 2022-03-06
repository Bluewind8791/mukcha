package com.mukcha.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Optional;

import com.mukcha.domain.Authority;
import com.mukcha.domain.Gender;
import com.mukcha.domain.User;
import com.mukcha.service.helper.WithUserTest;

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


    @Test // 22.3.5
    @DisplayName("1. 사용자 생성 테스트")
    void test_1() {
        // create
        User user = userTestHelper.createUser("ben@user.test", "TestUser");
        // find
        User savedUser = userService.findUser(user.getUserId()).get();
        // test
        assertEquals("ben@user.test", savedUser.getEmail());
        assertEquals("TestUser", savedUser.getNickname());
    }

    @Test // 22.3.5
    @DisplayName("2. 닉네임은 중복될 수 없다")
    void test_2() {
        userTestHelper.createUser("test1@test.com", "TestUser");
        assertThrows(DataIntegrityViolationException.class, () -> userTestHelper.createUser("test2@test.com", "TestUser"));
    }

    @Test // 22.3.5
    @DisplayName("3. 이메일은 중복될 수 없다")
    void test_3() {
        userTestHelper.createUser("test1@test.com", "TestUser1");
        assertThrows(DataIntegrityViolationException.class, () -> userTestHelper.createUser("test1@test.com", "TestUser2"));
    }

    @DisplayName("4. nickname, profileImage, birthday, gender 수정")
    @Test
    void test_4() {
        userTestHelper.createUser("test@user.test", "UserTest");
        User savedUser = userService.findByEmail("test@user.test").get();

        userService.updateNickname(savedUser.getUserId(), "testname");
        userService.updateProfileImage(savedUser.getUserId(), "testImage");
        userService.updateBirthday(savedUser.getUserId(), LocalDate.of(1991, 12, 14));
        userService.updateGender(savedUser.getUserId(), Gender.MALE);

        User editedUser = userService.findByEmail("test@user.test").get();

        assertEquals("testname", editedUser.getNickname());
        assertEquals("testImage", editedUser.getProfileImage());
        assertEquals(LocalDate.of(1991, 12, 14), editedUser.getBirthday());
        assertEquals(Gender.MALE, editedUser.getGender());
    }

    @Test // 22.3.5
    @DisplayName("5. ADMIN 권한 부여")
    void test_5() {
        User user = userTestHelper.createUserWithAuth("ben@test.com", "ben", Authority.ROLE_USER); // make user
        userService.addAuthority(user.getUserId(), Authority.ROLE_ADMIN); // add authority
        User savedUser = userService.findUser(user.getUserId()).get();
        userTestHelper.assertUser(savedUser, "ben@test.com", "ben", Authority.ROLE_USER, Authority.ROLE_ADMIN);
    }

    @Test  // 22.3.5
    @DisplayName("6. ADMIN 권한 취소")
    void test_6() {
        User user = userTestHelper.createUserWithAuth("ben@test.com", "ben", Authority.ROLE_USER, Authority.ROLE_ADMIN);
        userService.removeAuthority(user.getUserId(), Authority.ROLE_ADMIN);
        User savedUser = userService.findUser(user.getUserId()).get();
        userTestHelper.assertUser(savedUser, "ben@test.com", "ben", Authority.ROLE_USER);
    }

    @DisplayName("7. UserSecurityService의 email로 검색할 수 있다")
    @Test
    void test_7() {
        userTestHelper.createUser("test@user.test", "TestUser");
        User savedUser = (User) userSecurityService.loadUserByUsername("test@user.test");
        userTestHelper.assertUser(savedUser, "test@user.test", "TestUser");
    }

    @DisplayName("8. 패스워드를 수정할 수 있다.")
    @Test
    void test_8() {
        User user = userTestHelper.createUser("test@user.test", "TestUser", "123456"); // make user
        userService.updatePassword(user.getUserId(), "111111"); // update password
        User savedUser = userService.findUser(user.getUserId()).get(); // find user
        assertEquals("111111", savedUser.getPassword()); // assert
    }

    @Test
    @DisplayName("9. 회원 탈퇴를 진행한다.")
    void test_9() {
        User user1 = userTestHelper.createUser("test1@user.test", "test1");
        User user2 = userTestHelper.createUser("test2@user.test", "test2");
        userService.disableUser(user1.getUserId());
        userService.disableUser(user2.getUserId());
        assertEquals(Optional.empty(), userService.findUser(user1.getUserId()));
        assertEquals(Optional.empty(), userService.findUser(user2.getUserId()));
    }




}
