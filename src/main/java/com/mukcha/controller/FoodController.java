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
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = "/menu")
public class FoodController {

    @Autowired private FoodService foodService;
    @Autowired private ReviewService reviewService;


    // VIEW - FOOD INFO AND REVIEWS
    @GetMapping(value = "/{foodId}")
    public String getFoodInfo(
        @PathVariable Long foodId,
        Model model,
        @AuthenticationPrincipal User user,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "size", defaultValue = "3") Integer size
    ) {
        // 해당 메뉴의 정보
        model.addAttribute("food", foodService.viewFoodDetail(foodId));
        // paging 처리한 해당 메뉴의 모든 리뷰 보기
        model.addAttribute("reviewList", 
            reviewService.findAllByFoodIdOrderByCreatedAtDesc(foodId, pageNum, size)
        );
        try { // 로그인한 유저가 리뷰를 적었다면
            Review writtenReview = reviewService.findReviewByFoodIdAndUserId(foodId, user.getUserId());
            log.info("writtenReview: "+writtenReview);
            model.addAttribute("isReviewed", "true");
            model.addAttribute("writtenReview", writtenReview);
        } catch (Exception e) { // 리뷰를 쓰지 않은 로그인 한 유저
            model.addAttribute("isReviewed", "false");
        }
        return "food/detail";
    }


    // SAVE - SCORE AND COMMENT
    @PostMapping(value = "/api/review/{foodId}")
    public String setReview(
        @PathVariable Long foodId,
        Model model,
        ReviewDto reviewDto,
        @AuthenticationPrincipal User user
    ) {
        Food food = foodService.findFood(foodId).get();
        Review review = reviewService.saveReview(reviewDto.getScore(), reviewDto.getComment(), food, user);
        log.info(">>> Review Saved! "+review.toString());
        return "redirect:/menu/"+foodId;
    }


    // SAVE - EATEN DATE
    @PostMapping(value = "/api/eaten/{foodId}")
    public String setEatenDate(
        @PathVariable Long foodId,
        Model model,
        ReviewDto reviewDto,
        @AuthenticationPrincipal User user
    ) {
        Food food = foodService.findFood(foodId).get();
        Review review = reviewService.saveEatenDate(reviewDto.getEatenDate(), food, user);
        log.info(">>> Review Saved! "+review.toString());
        return "redirect:/menu/"+foodId;
    }


    // DELETE - REVIEW
    @DeleteMapping(value = "/review/{foodId}")
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


    // VIEW - 해당 메뉴의 모든 리뷰 보기
    @GetMapping(value = "/reviews/{foodId}")
    public String viewAllReviews(@PathVariable Long foodId, Model model) {
        model.addAttribute("foodName", foodService.findFood(foodId).get().getName());
        model.addAttribute("reviewList", reviewService.findAllReviewByFoodId(foodId));
        return "food/reviews";
    }





}
