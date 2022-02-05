package com.mukcha.backend.repository;

import java.util.List;

import com.mukcha.backend.domain.Category;
import com.mukcha.backend.domain.Food;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FoodRepository extends JpaRepository<Food, Long> {

    @Query("select distinct(category) from Food")
    List<String> getAllCategories();

    List<Food> findAllByCategory(Category category);
    
}
