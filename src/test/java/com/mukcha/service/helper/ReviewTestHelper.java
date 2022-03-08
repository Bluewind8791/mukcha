package com.mukcha.service.helper;


import com.mukcha.domain.Food;
import com.mukcha.domain.Review;
import com.mukcha.domain.Score;
import com.mukcha.domain.User;
import com.mukcha.service.ReviewService;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class ReviewTestHelper {
    
    private final ReviewService reviewService;

    public Review makeReview(Food food, User user) {
        Review review = new Review();
        review.setFood(food);
        review.setUser(user);
        review.setScore(Score.BAD);
        return review;
    }

    public Review createReview(Food food, User user) {
        return reviewService.save(makeReview(food, user));
    }

    public Review createReviewWithScore(Food food, User user, Score score) {
        Review review = makeReview(food, user);
        review.setScore(score);
        return reviewService.save(review);
    }



}
