package com.mukcha.service.helper;


import com.mukcha.domain.Food;
import com.mukcha.domain.Review;
import com.mukcha.domain.User;
import com.mukcha.repository.ReviewRepository;

import org.springframework.beans.factory.annotation.Autowired;



public class ReviewTestHelper {
    
    @Autowired private ReviewRepository reviewRepository;

    public Review makeReview(Food food, User user) {
        Review review = new Review();
        review.setFood(food);
        review.setUser(user);
        return review;
    }

    public Review createReview(Food food, User user) {
        return reviewRepository.save(makeReview(food, user));
    }


    
}
