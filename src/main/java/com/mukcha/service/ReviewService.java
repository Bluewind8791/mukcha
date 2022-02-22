package com.mukcha.service;

import java.time.LocalDate;
import java.util.Optional;

import com.mukcha.domain.Score;
import com.mukcha.repository.ReviewRepository;
import com.mukcha.domain.Review;

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


    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    public Optional<Review> findReview(Long reviewId) {
        return reviewRepository.findById(reviewId);
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
