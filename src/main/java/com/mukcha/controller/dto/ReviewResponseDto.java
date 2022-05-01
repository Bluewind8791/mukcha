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

    public ReviewResponseDto(Review entity) {
        this.comment = entity.getComment();
        this.score = entity.getScore();
        this.userName = entity.getUser().getNickname();
        if (entity.getEatenDate() != null) {
            this.eatenDate = entity.getEatenDate().toString();
        }
    }
}
