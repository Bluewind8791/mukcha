package com.mukcha.backend.service;

import com.mukcha.backend.domain.Review;
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



}
