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
0.5 : 쓰레기는 쓰레기통으로
1.0 : 버리진 마세요.
1.5 : 배고파서 먹어요.
2.0 : 음식이 되다가 말았습니다.
2.5 : 음식입니다.
3.0 : 흔한 맛
3.5 : 맛있습니다.
4.0 : 가끔 생각날 것 같아요.
4.5 : 특별한 날에 먹고 싶어요.
5.0 : 인생은 이 음식을 먹기 전과 후로 나뉩니다.
*/