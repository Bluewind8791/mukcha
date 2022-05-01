package com.mukcha.controller;

import java.util.List;

import com.mukcha.config.dto.LoginUser;
import com.mukcha.config.dto.SessionUser;
import com.mukcha.controller.dto.UserDto;
import com.mukcha.controller.dto.UserResponseDto;
import com.mukcha.domain.Category;
import com.mukcha.domain.User;
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


    // >>> VIEW <<<
    // Root page
    @GetMapping(value = {"/", ""})
    public String home(Model model, @LoginUser SessionUser sessionUser) {
        // login user 정보
        if (sessionUser != null) {
            UserResponseDto user = userService.getSessionUserInfo(sessionUser);
            model.addAttribute("login_user_id", user.getUserId());
            model.addAttribute("login_user_nickname", user.getNickname());
        }
        // 별점순 TOP 10 메뉴
        model.addAttribute("scoreTopTen", foodService.findTopTenOrderByScore());
        // 최신 메뉴 TOP 10
        model.addAttribute("newestTen", foodService.findTopTenNewest());
        
        return "home";
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String login(Model model) {
        return "user/loginForm";
    }


    // 유저 정보
    @GetMapping(value = "/users/{userId}")
    public String viewUserInfo(
        @PathVariable Long userId,
        Model model,
        @LoginUser SessionUser sessionUser
    ) {
        // Login User
        if (sessionUser != null) {
            UserResponseDto user = userService.getSessionUserInfo(sessionUser);
            model.addAttribute("login_user_id", user.getUserId());
            model.addAttribute("login_user_nickname", user.getNickname());
            model.addAttribute("login_email", user.getEmail());
        }
        // Category List
        List<Category> categoryList = List.of(Category.values());
        model.addAttribute("categoryList", categoryList);
        // DTO로 변환한 유저 정보 -> 서비스단으로 옮길 것
        User user = userService.findUser(userId);
        UserDto userDto = UserDto.builder()
                            .email(user.getEmail())
                            .nickname(user.getNickname())
                            .profileImage(user.getProfileImage())
                            .build()
        ;
        model.addAttribute("user", userDto);
        // 각 카테고리별 적은 리뷰 개수
        model.addAttribute("reviewCount", reviewService.getReviewCountByCategoryAndUserId(userId));
        return "user/userPage";
    }


    // 해당 유저의 각 카테고리별 리뷰
    @GetMapping(value = "/users/{userId}/category/{category}")
    public String viewReviewInCategory(
        @PathVariable Long userId,
        @PathVariable Category category,
        Model model,
        @LoginUser SessionUser sessionUser
    ) {
        // Login User
        if (sessionUser != null) {
            UserResponseDto user = userService.getSessionUserInfo(sessionUser);
            model.addAttribute("login_user_id", user.getUserId());
            model.addAttribute("login_user_nickname", user.getNickname());
        }
        model.addAttribute("reviewList", 
            reviewService.getReviewByCategoryAndUserId(userId, category)
        );
        return "food/reviews";
    }


}
/*

*/