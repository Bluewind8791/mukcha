package com.mukcha.controller;

import java.time.LocalDate;

import com.mukcha.domain.Gender;

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
