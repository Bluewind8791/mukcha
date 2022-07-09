package com.mukcha.controller;

import java.util.HashMap;
import java.util.Map;

import com.mukcha.config.dto.LoginUser;
import com.mukcha.config.dto.SessionUser;
import com.mukcha.service.FoodService;
import com.mukcha.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@Tag(name = "Home Controller")
public class HomeController {

    private final FoodService foodService;
    private final UserService userService;


    @GetMapping({"/", ""})
    @Operation(summary = "루트 페이지의 별점순 TOP10 메뉴와 최신메뉴 TOP10 리스트를 넘겨줍니다.")
    public ModelAndView home(@Parameter(hidden = true) @LoginUser SessionUser sessionUser) {
        Map<String, Object> response = new HashMap<>();
        // login user 정보
        if (sessionUser != null) {
            response.put("loginUser", userService.getSessionUserInfo(sessionUser));
        }
        // 별점순 TOP 10 메뉴
        response.put("scoreTopTen", foodService.findTopTenOrderByScore());
        // 최신 메뉴 TOP 10
        response.put("newestTen", foodService.findTopTenNewest());
        return new ModelAndView("home", response, HttpStatus.OK);
    }

    @GetMapping("/login")
    @Operation(summary = "로그인 페이지")
    public ModelAndView login() {
        return new ModelAndView("user/loginForm", HttpStatus.OK);
    }

    @GetMapping("/access-denied")
    @Operation(summary = "권한없음 페이지")
    public ModelAndView accessDenied(@LoginUser SessionUser sessionUser) {
        Map<String, Object> response = new HashMap<>();
        if (sessionUser != null) {
            response.put("loginUser", userService.getSessionUserInfo(sessionUser));
        }
        return new ModelAndView("forbidden", response, HttpStatus.OK);
    }

}