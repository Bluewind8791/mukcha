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
        Review review = Review.builder()
                            .food(food)
                            .user(user)
                            .score(Score.BAD)
                            .build();
        return review;
    }

    public Review createReview(Food food, User user) {
        return reviewService.save(makeReview(food, user));
    }

    public Review createReviewWithScore(Food food, User user, Score score) {
        Review review = makeReview(food, user);
        review.update(score, review.getComment());
        return reviewService.save(review);
    }



}
