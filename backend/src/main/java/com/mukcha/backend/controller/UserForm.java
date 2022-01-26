package com.mukcha.backend.controller;

import java.time.LocalDate;

import com.mukcha.backend.domain.Gender;

import lombok.Data;

@Data
public class UserForm {
    
    private String username;

    private String password;

    private String email;

    private LocalDate birthday;

    private String profileImage;

    private Gender gender;

}
