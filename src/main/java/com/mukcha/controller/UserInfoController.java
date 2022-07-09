package com.mukcha.controller;


import com.mukcha.config.dto.LoginUser;
import com.mukcha.config.dto.SessionUser;
import com.mukcha.controller.dto.SessionUserResponseDto;
import com.mukcha.controller.dto.UserResponseDto;
import com.mukcha.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;


// 
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/user")
@Tag(name = "User Info Controller", description = "ROLE_USER 및 ADMIN의 권한을 가진 사용자가 진입가능한 페이지입니다.")
public class UserInfoController {

    private final UserService userService;


    @GetMapping("/edit")
    @Operation(summary = "회원 개인정보 수정 페이지")
    public String viewUserEditPage(Model model, @LoginUser SessionUser sessionUser) {
        if (sessionUser != null) {
            SessionUserResponseDto sUser = userService.getSessionUserInfo(sessionUser);
            model.addAttribute("loginUser", sUser);
            UserResponseDto user = userService.findByUserId(sUser.getUserId());
            model.addAttribute("user", user);
        }
        return "user/editForm";
    }

    @GetMapping(value = "/delete")
    @Operation(summary = "회원 탈퇴 페이지")
    public String viewDisableUserPage(@LoginUser SessionUser sessionUser, Model model) {
        // Login User
        if (sessionUser != null) {
            model.addAttribute("loginUser", userService.getSessionUserInfo(sessionUser));
        }
        return "user/deleteForm";
    }

}