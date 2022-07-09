package com.mukcha.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mukcha.config.dto.LoginUser;
import com.mukcha.config.dto.SessionUser;
import com.mukcha.domain.Category;
import com.mukcha.service.ReviewService;
import com.mukcha.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "User Controller")
@RequestMapping(value = "/users")
public class UserController {

    private final ReviewService reviewService;
    private final UserService userService;


    @GetMapping("/{userId}")
    @Operation(summary = "해당 유저의 정보를 볼 수 있는 페이지")
    public ModelAndView viewUserInfo(
        @Parameter(description = "유저ID", example = "1") @PathVariable Long userId,
        @Parameter(hidden = true) @LoginUser SessionUser sessionUser
    ) {
        Map<String, Object> response = new HashMap<>();
        if (sessionUser != null) {
            response.put("loginUser", userService.getSessionUserInfo(sessionUser));
        }
        // 해당 유저가 각 카테고리별로 적은 리뷰의 개수
        response.put("reviewCount", reviewService.getCountByCategoryAndUserId(userId));
        // 모든 카테고리 리스트
        response.put("categoryList", List.of(Category.values()));
        // 해당 유저의 정보
        response.put("user", userService.findByUserId(userId));
        return new ModelAndView("user/userPage", response, HttpStatus.OK);
    }

    @GetMapping("/{userId}/category/{category}")
    @Operation(summary = "해당 유저의 각 카테고리별 리뷰를 보여주는 페이지")
    public ModelAndView viewReviewInCategory(
        @Parameter(description = "유저ID", example = "1") @PathVariable Long userId,
        @Parameter(description = "카테고리", example = "1") @PathVariable Category category,
        @LoginUser SessionUser sessionUser
    ) {
        Map<String, Object> response = new HashMap<>();
        if (sessionUser != null) {
            response.put("loginUser", userService.getSessionUserInfo(sessionUser));
        }
        response.put("reviewList", reviewService.findAllByCategoryAndUserId(userId, category));
        return new ModelAndView("food/reviews", response, HttpStatus.OK);
    }

}