package com.mukcha.controller.dto;

import javax.validation.constraints.NotBlank;

import com.mukcha.domain.Score;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReviewDto {

    private String comment;

    @NotBlank(message = "점수 평가는 필수 항목입니다.")
    private String rating;

    private String eatenDate;

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

}

/*
BAD(1), // 1 완전 별로였어요
NOTGOOD(2), // 2 추천하고 싶지 않아요
SOSO(3), // 3 한번은 먹을만해요
GOOD(4), // 4 또 먹고싶어요
BEST(5); // 5 인생메뉴에요!
*/