package com.mukcha.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequestDto {

    private String nickname;
    private String profileImage;
    private String gender;
    private String birthYear;

    @Builder
    public UserUpdateRequestDto(String nickname, String profileImage, String gender, String birthYear) {
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.gender = gender;
        this.birthYear = birthYear;
    }

}