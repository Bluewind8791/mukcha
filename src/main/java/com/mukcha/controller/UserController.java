package com.mukcha.controller;


import com.mukcha.config.dto.LoginUser;
import com.mukcha.config.dto.SessionUser;
import com.mukcha.controller.dto.SessionUserResponseDto;
import com.mukcha.controller.dto.UserResponseDto;
import com.mukcha.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;


// ROLE_USER 권한 있어야 진입가능
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;


    // 회원 개인정보 수정 페이지
    @GetMapping("/edit")
    public String viewUserEditPage(Model model, @LoginUser SessionUser sessionUser) {
        // Login User
        if (sessionUser != null) {
            SessionUserResponseDto sUser = userService.getSessionUserInfo(sessionUser);
            model.addAttribute("loginUser", sUser);
            UserResponseDto user = userService.findByUserId(sUser.getUserId());
            model.addAttribute("user", user);
        }
        return "user/editForm";
    }

    // 회원 탈퇴 페이지
    @GetMapping(value = "/delete")
    public String viewDisableUserPage(@LoginUser SessionUser sessionUser, Model model) {
        // Login User
        if (sessionUser != null) {
            model.addAttribute("loginUser", userService.getSessionUserInfo(sessionUser));
        }
        return "user/deleteForm";
    }


}