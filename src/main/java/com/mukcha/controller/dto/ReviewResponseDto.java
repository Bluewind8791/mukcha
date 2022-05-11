package com.mukcha.controller.dto;

import com.mukcha.domain.Review;
import com.mukcha.domain.Score;

import lombok.Getter;


@Getter
public class ReviewResponseDto {

    private String comment;
    private Score score;
    private String eatenDate;
    private String userName;
    private String foodName;
    private Long foodId;
    private Long userId;

    public ReviewResponseDto(Review entity) {
        this.comment = entity.getComment();
        this.score = entity.getScore();
        this.userName = entity.getUser().getNickname();
        this.foodName = entity.getFood().getName();
        this.foodId = entity.getFood().getFoodId();
        this.userId = entity.getUser().getUserId();
        if (entity.getEatenDate() != null) {
            this.eatenDate = entity.getEatenDate().toString();
        }
    }
}
