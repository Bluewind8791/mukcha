package com.mukcha.controller;

import com.mukcha.config.dto.LoginUser;
import com.mukcha.config.dto.SessionUser;
import com.mukcha.controller.dto.EatenDateSaveRequestDto;
import com.mukcha.controller.dto.ReviewSaveRequestDto;
import com.mukcha.service.ReviewService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class ReviewApiController {

    private final ReviewService reviewService;


    // 점수와 코멘트를 저장 및 수정
    @PostMapping(value = "/api/reviews/{foodId}")
    public String saveReview(
        @PathVariable Long foodId,
        @ModelAttribute ReviewSaveRequestDto requestDto,
        @LoginUser SessionUser user
    ) {
        reviewService.saveReview(foodId, user.getEmail(), requestDto);
        return "redirect:/menu/"+foodId;
    }

    // 먹은 날짜를 저장 및 수정
    @PostMapping(value = "/api/reviews/eaten/{foodId}")
    public String saveEatenDate(
        @PathVariable Long foodId,
        EatenDateSaveRequestDto requestDto,
        @LoginUser SessionUser user
    ) {
        reviewService.saveEatenDate(foodId, user.getEmail(), requestDto.getEatenDate());
        return "redirect:/menu/"+foodId;
    }

    // 해당 유저가 작성한 해당 음식의 리뷰를 삭제한다.
    @DeleteMapping(value = "/api/reviews/{foodId}")
    public String deleteScore(@PathVariable Long foodId, @LoginUser SessionUser user) {
        reviewService.deleteReview(foodId, user.getEmail());
        return "redirect:/menu/"+foodId;
    }

}
