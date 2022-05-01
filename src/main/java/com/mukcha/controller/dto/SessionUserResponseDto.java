package com.mukcha.controller.dto;

import com.mukcha.domain.User;

import lombok.Getter;

@Getter
public class SessionUserResponseDto {

    private Long userId;
    private String userEmail;
    private String userName;

    public SessionUserResponseDto(User entity) {
        this.userId = entity.getUserId();
        this.userEmail = entity.getEmail();
        this.userName = entity.getNickname();
    }

}
