package com.mukcha.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mukcha.controller.dto.SessionUserResponseDto;
import com.mukcha.controller.dto.UserUpdateRequestDto;
import com.mukcha.controller.helper.WithMockMvcTest;
import com.mukcha.domain.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@ActiveProfiles("test")
@WithMockUser(roles = "USER")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserApiControllerTest extends WithMockMvcTest {

    @BeforeEach
    void before() {
        prepareTest();
        User user = createUser("user@test.com", "ben");
        this.userResDto = new SessionUserResponseDto(user);
        mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
    }

    @Test
    @DisplayName("회원 개인정보를 업데이트한다")
    void testUpdateUserInfo() throws JsonProcessingException, Exception {
        // given
        User user = userService.findUser(userResDto.getUserId());
        Long userId = user.getUserId();
        String expectNickname = "changed";
        String expectGender = "MALE";
        String expectBirth = "1991";
        String expectProfileImage = "profileImage";
        String url = "http://localhost:"+port+"/api/users/"+userId;
        UserUpdateRequestDto requestDto = UserUpdateRequestDto.builder()
            .nickname(expectNickname)
            .gender(expectGender)
            .birthYear(expectBirth)
            .profileImage(expectProfileImage)
            .build();
        // when
        mvc.perform(MockMvcRequestBuilders
            .put(url)
            .requestAttr("loginUser", userResDto)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(requestDto))
        ).andExpect(MockMvcResultMatchers.status().isOk());
        // then
        User expectUser = userService.findUser(userId);
        assertEquals(expectNickname, expectUser.getNickname());
        assertEquals(expectGender, expectUser.getGender().name());
        assertEquals(expectBirth, expectUser.getBirthYear().toString());
        assertEquals(expectProfileImage, expectUser.getProfileImage());
    }



    @Test
    @DisplayName("회원을 탈퇴한다")
    void testDisableUser() throws Exception {
        // given
        User user = userService.findUser(userResDto.getUserId());
        Long userId = user.getUserId();
        String url = "http://localhost:"+port+"/api/users/"+userId;
        // when
        mvc.perform(MockMvcRequestBuilders
            .patch(url)
            .requestAttr("loginUser", userResDto)
        ).andExpect(MockMvcResultMatchers.status().isOk());
        // then
        assertThrows(IllegalArgumentException.class, () -> userService.findUser(userId));
    }


}
