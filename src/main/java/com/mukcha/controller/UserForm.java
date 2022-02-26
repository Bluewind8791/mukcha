package com.mukcha.controller;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserForm {

    @Email(message = "이메일 형식에 맞지 않습니다.")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    @Size(min = 2)
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    @Pattern(
        regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{4,20}",
        message = "비밀번호는 영문 대,소문자와 숫자가 포함된 4자 ~ 20자의 비밀번호여야 합니다."
    )
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    @NotBlank(message = "비밀번호를 확인해 주세요.")
    private String rePassword;
    
    private String gender;

    // @Pattern(
    //     regexp = "([0-9]+).{4}",
    //     message = "숫자만 입력해주세요. (4자)"
    // )
    private String birthYear;

    private String birthMonth;

    // @Pattern(
    //     regexp = "[0-9]+",
    //     message = "숫자만 입력해주세요."
    // )
    private String birthDay;

    // private String profileImage;


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