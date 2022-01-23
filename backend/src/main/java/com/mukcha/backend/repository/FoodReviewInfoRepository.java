package com.mukcha.backend.repository;

import com.mukcha.backend.domain.FoodReviewInfo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodReviewInfoRepository extends JpaRepository<FoodReviewInfo, Long> {
    
}
