package com.mukcha.backend.service;

import java.time.LocalDate;

import com.mukcha.backend.domain.Review;
import com.mukcha.backend.domain.Score;
import com.mukcha.backend.repository.ReviewRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }


    public Review findById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(
            () -> new IllegalArgumentException("찾을 수 없는 리뷰입니다.")
        );
    }

    public void setReviewComment(Review review, String comment) {
        if (review.getScore() == null) {
            throw new IllegalArgumentException("점수를 먼저 매겨주세요!");
        }
        review.setComment(comment);
    }

    public void setReviewEatenDate(Review review, LocalDate eatenDate) {
        if (review.getScore() == null) {
            throw new IllegalArgumentException("점수를 먼저 매겨주세요!");
        }
        review.setEatenDate(eatenDate);
    }


    public void editReviewScore(Review review, Score score) {
        review.setScore(score);
    }


    public void editReviewComment(Review review, String comment) {
        review.setComment(comment);
    }


    public void editReviewEatenDate(Review review, LocalDate eatenDate) {
        review.setEatenDate(eatenDate);
    }


    public void deleteScore(Review review) {
        reviewRepository.deleteById(review.getReviewId());
    }



}
