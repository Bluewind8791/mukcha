package com.mukcha.service.helper;


import java.time.LocalDate;

import com.mukcha.domain.Food;
import com.mukcha.domain.Review;
import com.mukcha.domain.Score;
import com.mukcha.domain.User;
import com.mukcha.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class ReviewTestHelper {
    
    private final ReviewRepository reviewRepository;

    public Review makeReview(Food food, User user) {
        Review review = new Review();
        review.setFood(food);
        review.setUser(user);
        review.setComment("wow!");
        review.setScore(Score.GOOD);
        review.setEatenDate(LocalDate.now());
        return review;
    }

    public Review createReview(Food food, User user) {
        return reviewRepository.save(makeReview(food, user));
    }

    public Review createReviewWithScore(Food food, User user, Score score) {
        Review review = makeReview(food, user);
        review.setScore(score);
        return reviewRepository.save(review);
    }



}
