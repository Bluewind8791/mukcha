package com.mukcha.controller;


import com.mukcha.controller.dto.ReviewDto;
import com.mukcha.domain.Food;
import com.mukcha.domain.Review;
import com.mukcha.domain.User;
import com.mukcha.service.FoodService;
import com.mukcha.service.ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/menu")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @Autowired
    private ReviewService reviewService;


    @GetMapping(value = "/{foodId}")
    public String getFoodInfo(
            @PathVariable Long foodId,
            Model model
        ) {

        model.addAttribute("food", foodService.viewFoodDetail(foodId));
        // 모든 리뷰 보기
        model.addAttribute("reviewList", reviewService.findReviewByFoodId(foodId));

        System.out.println(">>> getReviewList: "+reviewService.findReviewByFoodId(foodId));
        return "food/detail";
    }


    @PostMapping(value = "/api/score/{foodId}")
    public String setScore(
            @PathVariable Long foodId,
            Model model,
            ReviewDto reviewDto,
            @AuthenticationPrincipal User user
    ) {

        Food food = foodService.findFood(foodId).get();
        System.out.println(">>> reviewDto: "+reviewDto);

        Review review = reviewService.saveReview(reviewDto.getScore(), reviewDto.getComment(), food, user);

        log.info(">>> Review Saved! "+review.toString());
        return "redirect:/menu/"+foodId;
    }


    @DeleteMapping(value = "/score/{foodId}")
    public String deleteScore(@PathVariable Long foodId, @AuthenticationPrincipal User user) {

        Food targetFood = foodService.findFood(foodId).orElseThrow(() -> 
            new IllegalArgumentException("해당 메뉴를 찾을 수 없습니다.")
        );
        // 현재 로그인한 유저가 작성한 해당 음식의 리뷰를 찾는다.
        Review targetReview = reviewService.findReviewByFoodIdAndUserId(targetFood.getFoodId(), user.getUserId());

        // delete
        log.info(user.getEmail() +"님이 <"+targetFood.getName()+"> 의 리뷰를 삭제합니다.");
        reviewService.deleteReview(targetReview);

        return "redirect:/menu/"+foodId;
    }






}
