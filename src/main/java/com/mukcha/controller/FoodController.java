package com.mukcha.controller;


import com.mukcha.config.dto.LoginUser;
import com.mukcha.config.dto.SessionUser;
import com.mukcha.controller.dto.ReviewDto;
import com.mukcha.controller.dto.UserDto;
import com.mukcha.domain.Food;
import com.mukcha.domain.Review;
import com.mukcha.service.FoodService;
import com.mukcha.service.ReviewService;
import com.mukcha.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/menu")
public class FoodController {

    private final FoodService foodService;
    private final ReviewService reviewService;
    private final UserService userService;


    // >>> VIEW <<<
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
            UserDto user = userService.getSessionUserInfo(sessionUser);
            model.addAttribute("userId", user.getUserId());
            model.addAttribute("nickname", user.getNickname());
            try { // 로그인한 유저가 리뷰를 적었다면
                Review writtenReview = reviewService.findReviewByFoodIdAndUserId(foodId, user.getUserId());
                log.info("writtenReview: "+writtenReview);
                model.addAttribute("isReviewed", "true");
                model.addAttribute("writtenReview", writtenReview);
            } catch (Exception e) { // 리뷰를 쓰지 않은 로그인 한 유저
                model.addAttribute("isReviewed", "false");
            }
        }
        // 해당 메뉴의 정보
        model.addAttribute("food", foodService.viewFoodDetail(foodId));
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
            UserDto user = userService.getSessionUserInfo(sessionUser);
            model.addAttribute("userId", user.getUserId());
            model.addAttribute("nickname", user.getNickname());
        }
        model.addAttribute("foodName", foodService.findFood(foodId).get().getName());
        model.addAttribute("reviewList", reviewService.findAllReviewByFoodId(foodId));
        return "food/reviews";
    }




    // >>> METHODS <<<

    // 점수와 코멘트를 저장
    @PostMapping(value = "/api/review/{foodId}")
    public String setReview(
        @PathVariable Long foodId,
        Model model,
        ReviewDto reviewDto,
        @LoginUser SessionUser sessionUser
    ) {
        if (sessionUser != null) {
            UserDto user = userService.getSessionUserInfo(sessionUser);
            model.addAttribute("userId", user.getUserId());
            model.addAttribute("nickname", user.getNickname());
            Review review = reviewService.saveReview(reviewDto.getScore(), reviewDto.getComment(), foodId, user.getUserId());
            log.info(">>> 회원<"+user.getEmail()+">님의 리뷰가 처리되었습니다."+review.toString());
        }
        return "redirect:/menu/"+foodId;
    }


    // 먹은 날짜를 저장
    @PostMapping(value = "/api/eaten/{foodId}")
    public String setEatenDate(
        @PathVariable Long foodId,
        Model model,
        ReviewDto reviewDto,
        @LoginUser SessionUser sessionUser
    ) {
        if (sessionUser != null) {
            UserDto user = userService.getSessionUserInfo(sessionUser);
            Review review = reviewService.saveEatenDate(reviewDto.getEatenDate(), foodId, user.getUserId());
            log.info(">>> 회원<"+user.getEmail()+">님의 리뷰가 처리되었습니다."+review.toString());
        }
        return "redirect:/menu/"+foodId;
    }


    // 리뷰를 삭제
    @DeleteMapping(value = "/review/{foodId}")
    public String deleteScore(@PathVariable Long foodId, @LoginUser SessionUser sessionUser) {
        Food targetFood = foodService.findFood(foodId).orElseThrow(() -> 
            new IllegalArgumentException("해당 메뉴를 찾을 수 없습니다.")
        );
        if (sessionUser != null) {
            UserDto user = userService.getSessionUserInfo(sessionUser);
            // 현재 로그인한 유저가 작성한 해당 음식의 리뷰를 찾는다.
            Review targetReview = reviewService.findReviewByFoodIdAndUserId(foodId, user.getUserId());
            // 해당 음식의 리뷰를 삭제한다.
            reviewService.deleteReview(targetReview);
            log.info(">>> 회원 <"+user.getEmail()+"> 님의 <"+targetFood.getName()+"> 리뷰가 삭제 처리되었습니다.");
        }
        return "redirect:/menu/"+foodId;
    }


}
