package com.mukcha.controller;


import com.mukcha.config.dto.LoginUser;
import com.mukcha.config.dto.SessionUser;
import com.mukcha.controller.dto.SessionUserResponseDto;
import com.mukcha.service.FoodService;
import com.mukcha.service.ReviewService;
import com.mukcha.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/menu")
public class FoodController {

    private final FoodService foodService;
    private final ReviewService reviewService;
    private final UserService userService;


    // 해당 메뉴의 정보와 리뷰 3개를 보여주는 페이지
    @GetMapping(value = "/{foodId}")
    public String getFoodInfo(
        @PathVariable Long foodId,
        Model model,
        @LoginUser SessionUser sessionUser,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "size", defaultValue = "3") Integer size
    ) {
        if (sessionUser != null) {
            SessionUserResponseDto user = userService.getSessionUserInfo(sessionUser);
            model.addAttribute("loginUser", user);
            // 로그인 한 유저가 리뷰를 적었다면
            if (reviewService.isUserWriteReviewOnFood(foodId, user.getUserId())) {
                model.addAttribute("isReviewed", "true");
                model.addAttribute("writtenReview", 
                    reviewService.findDtoByFoodIdAndUserId(foodId, user.getUserId())
                );
            } else { // 로그인 한 유저가 리뷰를 적지 않았다면
                model.addAttribute("isReviewed", "false");
            }
        }
        // 해당 메뉴의 정보
        model.addAttribute("food", foodService.findFood(foodId));
        // paging 처리한 해당 메뉴의 모든 리뷰 보기
        model.addAttribute("reviewList", 
            reviewService.findAllByFoodIdOrderByCreatedAtDesc(foodId, pageNum, size)
        );
        return "food/detail";
    }

    // '리뷰 더보기'를 눌러 해당 메뉴의 모든 리뷰 보기
    @GetMapping(value = "/reviews/{foodId}")
    public String viewAllReviews(@PathVariable Long foodId, Model model, @LoginUser SessionUser sessionUser) {
        if (sessionUser != null) {
            model.addAttribute("loginUser", userService.getSessionUserInfo(sessionUser));
        }
        // model.addAttribute("foodName", foodService.findByFoodId(foodId).getName()); // 해당 메뉴 이름
        model.addAttribute("reviewList", reviewService.findAllByFoodId(foodId)); // 메뉴의 모든 리뷰 리스트
        return "food/reviews";
    }


}
