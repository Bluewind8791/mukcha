package com.mukcha.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mukcha.controller.dto.UserUpdateRequestDto;
import com.mukcha.controller.helper.WithControllerTest;
import com.mukcha.domain.User;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserApiControllerTest extends WithControllerTest {


    @Test
    @DisplayName("유저가 회원 정보를 업데이트한다.")
    void testUpdateUserInfo() {
        // given
        User user = createUser("email@test.com", "testnick");
        Long userId = user.getUserId();
        String expectNickname = "changed";
        String expectGender = "MALE";
        String expectBirth = "1991";
        String expectProfileImage = "profileImage";
        String url = "http://localhost:"+port+"/api/users/update";
        UserUpdateRequestDto requestDto = UserUpdateRequestDto.builder()
            .nickname(expectNickname)
            .gender(expectGender)
            .birthYear(expectBirth)
            .profileImage(expectProfileImage)
            .build();
        HttpEntity<UserUpdateRequestDto> httpEntity = new HttpEntity<>(requestDto);
        // when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, Long.class);
        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        User expectUser = userRepository.findById(userId).get();
        assertEquals(expectNickname, expectUser.getNickname());
        assertEquals(expectGender, expectUser.getGender().name());
        assertEquals(expectBirth, expectUser.getBirthYear().toString());
        assertEquals(expectProfileImage, expectUser.getProfileImage());
    }



    @Test
    @DisplayName("유저가 회원을 탈퇴한다.")
    void testDisableUser() {
        // given
        createUser("email@test.com", "testnick");
        // String url = "http://localhost:"+port+"/api/users/disable";
        
        // HttpEntity<> httpEntity = new HttpEntity<T>(requestDto);

        // when
        // ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        // then
    }


}
