package com.mukcha.repository;

import java.util.List;

import com.mukcha.domain.Category;
import com.mukcha.domain.Food;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FoodRepository extends JpaRepository<Food, Long> {

    @Query("select distinct(category) from Food")
    List<String> getAllCategories();

    List<Food> findAllByCategory(Category category);
    
}
