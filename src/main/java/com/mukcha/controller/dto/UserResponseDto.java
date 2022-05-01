package com.mukcha.controller.dto;

import com.mukcha.domain.User;

import lombok.Getter;

@Getter
public class UserResponseDto {

    private Long userId;
    private String email;
    private String nickname;
    private String profileImage;
    private String gender;
    private String birthYear;

    public UserResponseDto(User entity) {
        this.userId = entity.getUserId();
        this.email = entity.getEmail();
        this.nickname = entity.getNickname();
        this.profileImage = entity.getProfileImage();
        if (entity.getGender() != null) {
            this.gender = entity.getGender().name();
        }
        if (entity.getBirthYear() != null) {
            this.birthYear = entity.getBirthYear();
        }
    }

}
