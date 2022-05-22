package com.mukcha.controller.api;

import com.mukcha.controller.dto.EatenDateSaveRequestDto;
import com.mukcha.controller.dto.ReviewSaveRequestDto;
import com.mukcha.service.ReviewService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewApiController {

    private final ReviewService reviewService;


    // 점수와 코멘트를 저장 및 수정
    @PostMapping("/users/{userId}/menus/{foodId}")
    public ResponseEntity<?> saveReview(
        @PathVariable Long foodId,
        @PathVariable Long userId,
        @RequestBody ReviewSaveRequestDto requestDto
    ) {
        reviewService.saveReview(userId, foodId, requestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 먹은 날짜를 저장 및 수정
    @PutMapping("/users/{userId}/menus/{foodId}")
    public ResponseEntity<?> saveEatenDate(
        @PathVariable Long foodId,
        @PathVariable Long userId,
        @RequestBody EatenDateSaveRequestDto requestDto
    ) {
        reviewService.saveEatenDate(userId, foodId, requestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 해당 유저가 작성한 해당 음식의 리뷰를 삭제한다.
    @DeleteMapping("/users/{userId}/menus/{foodId}")
    public ResponseEntity<?> deleteScore(
        @PathVariable Long foodId,
        @PathVariable Long userId
    ) {
        reviewService.deleteReview(userId, foodId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
