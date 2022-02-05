package com.mukcha.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.mukcha.backend.domain.Authority;
import com.mukcha.backend.domain.Gender;
import com.mukcha.backend.domain.User;
import com.mukcha.backend.repository.UserRepository;
import com.mukcha.backend.service.helper.UserTestHelper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@SpringBootTest
public class UserServiceTest {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;

    private UserTestHelper userTestHelper;

    @BeforeEach
    void before() {
        this.userRepository.deleteAll();
        this.userService = new UserService(userRepository);
        this.userTestHelper = new UserTestHelper(userService, NoOpPasswordEncoder.getInstance());
    }


    @DisplayName("1. 사용자를 생성 테스트")
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

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> userTestHelper.createUser("ben2@test.com", "ben"));
        Assertions.assertThat(ex.getMessage()).isEqualTo("이미 사용중인 아이디입니다.");
    }

    @DisplayName("3. 이메일은 중복될 수 없다")
    @Test
    void test_3() {
        userTestHelper.createUser("ben@test.com", "ben");

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> userTestHelper.createUser("ben@test.com", "ben2"));
        Assertions.assertThat(ex.getMessage()).isEqualTo("이미 사용중인 이메일입니다.");
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
        User user = userTestHelper.createUser("ben@test.com", "ben", Authority.ROLE_USER);
        userService.addAuthority(user.getUserId(), Authority.ROLE_ADMIN);

        List<User> list = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        assertEquals(Authority.ROLE_ADMIN, list.get(0).getAuthorities());
    }

    @DisplayName("6. ")
    @Test
    void test_6() {

    }


/**
 * 이메일을 제외한 이름, 패스워드, 기타 정보를 수정할 수 있다.
 * email로 검색할 수 있다.
 * email이 중복되어서 등록되지 않는다.
 */

    
}
