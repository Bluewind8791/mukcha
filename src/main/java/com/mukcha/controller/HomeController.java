package com.mukcha.controller;

import com.mukcha.config.dto.LoginUser;
import com.mukcha.config.dto.SessionUser;
import com.mukcha.service.FoodService;
import com.mukcha.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class HomeController {

    private final FoodService foodService;
    private final UserService userService;


    // Root page
    @GetMapping(value = {"/", ""})
    public String home(Model model, @LoginUser SessionUser sessionUser) {
        // login user 정보
        if (sessionUser != null) {
            model.addAttribute("loginUser", userService.getSessionUserInfo(sessionUser));
        }
        // 별점순 TOP 10 메뉴
        model.addAttribute("scoreTopTen", foodService.findTopTenOrderByScore());
        // 최신 메뉴 TOP 10
        model.addAttribute("newestTen", foodService.findTopTenNewest());
        return "home";
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String login() {
        return "user/loginForm";
    }

    // 권한없음 페이지
    @GetMapping("/access-denied")
    public String accessDenied(Model model, @LoginUser SessionUser sessionUser) {
        if (sessionUser != null) {
            model.addAttribute("loginUser", userService.getSessionUserInfo(sessionUser));
        }
        return "/forbidden";
    }

}