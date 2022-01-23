package com.mukcha.backend.repository;

import com.mukcha.backend.domain.Food;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {
    
}
