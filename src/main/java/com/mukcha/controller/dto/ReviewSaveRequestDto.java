package com.mukcha.controller.dto;

import javax.validation.constraints.NotBlank;

import com.mukcha.domain.Review;
import com.mukcha.domain.Score;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ReviewSaveRequestDto {

    @NotBlank(message = "점수 평가는 필수 항목입니다.")
    @Schema(description = "해당 리뷰 점수", required = true, example = "또 먹고 싶어요")
    private String rating;

    @Schema(description = "리뷰 코멘트", required = false, example = "맛있어요!")
    private String comment;


    @Builder
    public ReviewSaveRequestDto(String comment, String rating) {
        this.comment = comment;
        this.rating = rating;
    }

    public Review toEntity() {
        return Review.builder()
            .comment(comment)
            .score(transScore(rating))
            .build();
    }

    public Score getScore() {
        switch (this.rating) {
            case "인생 메뉴에요!":
                return Score.BEST;
            case "또 먹고 싶어요":
                return Score.GOOD;
            case "한 번은 먹을만해요":
                return Score.SOSO;
            case "추천하고 싶지 않아요":
                return Score.NOTGOOD;
            case "완전 별로였어요":
                return Score.BAD;
        }
        return null;
    }

    private Score transScore(String rating) {
        switch (rating) {
            case "인생 메뉴에요!":
                return Score.BEST;
            case "또 먹고 싶어요":
                return Score.GOOD;
            case "한 번은 먹을만해요":
                return Score.SOSO;
            case "추천하고 싶지 않아요":
                return Score.NOTGOOD;
            case "완전 별로였어요":
                return Score.BAD;
        }
        return null;
    }

}
