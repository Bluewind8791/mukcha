package com.mukcha.controller;

import com.mukcha.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;


// ROLE_USER 권한 있어야 진입가능
@Slf4j
@Controller
@RequestMapping(value = "/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    // 회원 정보 확인 및 수정
    @GetMapping("/edit")
    public String userPage() {
        log.info("회원 정보 수정 진입");
        return "user/userPage";
    }
    
}
