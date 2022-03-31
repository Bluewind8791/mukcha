package com.mukcha.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private Long userId;

    private String email;

    private String nickname;

    private String profileImage;

    private String gender;

    private String birthYear;

    @Builder
    public UserDto(Long userId, String email, String nickname, String profileImage, String gender, String birthYear) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.gender = gender;
        this.birthYear = birthYear;
    }

    public UserDto() {}

}
/*
(?=.*[0-9])
숫자 적어도 하나
(?=.*[a-zA-Z])
영문 대,소문자중 적어도 하나
(?=.*\\W)
특수문자 적어도 하나
(?=\\S+$)
공백 제거
정규표현식에 맞는 문자열이어야 함
*/