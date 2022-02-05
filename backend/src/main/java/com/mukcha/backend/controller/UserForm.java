package com.mukcha.backend.controller;

import java.time.LocalDate;

import com.mukcha.backend.domain.Gender;

import lombok.Data;

@Data
public class UserForm {

    private String email;
    private String password;
    private String nickname;
    private LocalDate birthday;
    private String profileImage;
    private Gender gender;

}
