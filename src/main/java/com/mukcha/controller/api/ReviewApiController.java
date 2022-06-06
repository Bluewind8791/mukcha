package com.mukcha.controller.api;

import com.mukcha.config.dto.SpringDocApiResponse;
import com.mukcha.controller.dto.EatenDateSaveRequestDto;
import com.mukcha.controller.dto.ReviewSaveRequestDto;
import com.mukcha.service.ReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Review API Controller",
    description = "ROLE_USER의 권한을 가진 사용자가 접근 가능한 리뷰 API입니다.")
public class ReviewApiController {

    private final ReviewService reviewService;


    @SpringDocApiResponse
    @PostMapping("/users/{userId}/menus/{foodId}")
    @Operation(summary = "점수와 코멘트를 저장 및 수정하는 메소드")
    public ResponseEntity<?> saveReview(
        @Parameter(description = "해당 메뉴 ID", required = true) @PathVariable Long foodId,
        @Parameter(description = "해당 회원 ID", required = true) @PathVariable Long userId,
        @Valid @RequestBody ReviewSaveRequestDto requestDto
    ) {
        reviewService.saveReview(userId, foodId, requestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @SpringDocApiResponse
    @PutMapping("/users/{userId}/menus/{foodId}")
    @Operation(summary = "먹은 날짜를 저장 및 수정하는 메소드")
    public ResponseEntity<?> saveEatenDate(
        @Parameter(description = "해당 메뉴 ID", required = true) @PathVariable Long foodId,
        @Parameter(description = "해당 회원 ID", required = true) @PathVariable Long userId,
        @RequestBody EatenDateSaveRequestDto requestDto
    ) {
        reviewService.saveEatenDate(userId, foodId, requestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @SpringDocApiResponse
    @Operation(summary = "해당 메뉴 리뷰 삭제 메소드")
    @DeleteMapping("/users/{userId}/menus/{foodId}")
    public ResponseEntity<?> deleteScore(
        @Parameter(description = "해당 메뉴 ID", required = true) @PathVariable Long foodId,
        @Parameter(description = "해당 회원 ID", required = true) @PathVariable Long userId
    ) {
        if (reviewService.deleteReview(userId, foodId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


}
