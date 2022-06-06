package com.mukcha.config.dto;

import java.io.Serializable;

import com.mukcha.domain.User;

import lombok.Getter;


// 직렬화 기능을 가진 User 클래스
@Getter
public class SessionUser implements Serializable {

    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.email = user.getEmail();
        this.name = user.getNickname();
        this.picture = user.getProfileImage();
    }

}
