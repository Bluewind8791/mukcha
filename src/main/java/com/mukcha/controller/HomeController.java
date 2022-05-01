package com.mukcha.controller;

import java.util.List;

import com.mukcha.config.dto.LoginUser;
import com.mukcha.config.dto.SessionUser;
import com.mukcha.domain.Category;
import com.mukcha.service.FoodService;
import com.mukcha.service.ReviewService;
import com.mukcha.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class HomeController {

    private final FoodService foodService;
    private final UserService userService;
    private final ReviewService reviewService;


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

    // 해당 유저 정보 페이지
    @GetMapping(value = "/users/{userId}")
    public String viewUserInfo(
        Model model,
        @PathVariable Long userId,
        @LoginUser SessionUser sessionUser
    ) {
        // Login User
        if (sessionUser != null) {
            model.addAttribute("loginUser", userService.getSessionUserInfo(sessionUser));
        }
        // Category List
        model.addAttribute("categoryList", List.of(Category.values()));
        // 해당 유저의 정보
        model.addAttribute("user", userService.findByUserId(userId));
        // 해당 유저가 각 카테고리별로 적은 리뷰의 개수
        model.addAttribute("reviewCount", reviewService.getCountByCategoryAndUserId(userId));
        return "user/userPage";
    }

    // 해당 유저의 각 카테고리별 리뷰 페이지
    @GetMapping(value = "/users/{userId}/category/{category}")
    public String viewReviewInCategory(
        @PathVariable Long userId,
        @PathVariable Category category,
        Model model,
        @LoginUser SessionUser sessionUser
    ) {
        // Login User
        if (sessionUser != null) {
            model.addAttribute("loginUser", userService.getSessionUserInfo(sessionUser));
        }
        model.addAttribute("reviewList", 
            reviewService.findAllByCategoryAndUserId(userId, category)
        );
        return "food/reviews";
    }


}