package com.mukcha.controller;

import lombok.Data;

@Data
public class UserForm {

    private String email;
    private String nickname;
    private String password;
    private String rePassword;
    private String gender;
    private String birthYear;
    private String birthMonth;
    private String birthDay;

    // private String profileImage;


}
