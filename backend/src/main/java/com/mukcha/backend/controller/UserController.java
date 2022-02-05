package com.mukcha.backend.controller;

import com.mukcha.backend.domain.User;
import com.mukcha.backend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입 url
    @GetMapping("/user/join")
    public String joinForm() {
        return "user/joinForm";
    }

    // 회원가입 처리
    @PostMapping("/user/join")
    public String join(UserForm form) {
        User user = User.builder()
            .email(form.getEmail())
            .password(form.getPassword())
            .nickname(form.getNickname())
            .gender(form.getGender())
            .birthday(form.getBirthday())
            .build();

        userService.join(user);
        log.info("회원가입이 처리되었습니다." + user.toString());
        return "redirect:/";
    }
    
}
