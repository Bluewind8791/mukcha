package com.mukcha.repository;

import com.mukcha.domain.FoodReviewInfo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodReviewInfoRepository extends JpaRepository<FoodReviewInfo, Long> {
    
}
