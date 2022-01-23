package com.mukcha.backend.repository;

import com.mukcha.backend.domain.Category;
import com.mukcha.backend.domain.Company;
import com.mukcha.backend.domain.Food;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FoodRepositoryTest {

    @Autowired
    private FoodRepository foodRepository;

    @DisplayName("1. Food Repository Test")
    @Test
    void test_1() {
        Food food = Food.builder()
                .name("원헌드RED")
                .company(new Company())
                .category(Category.CHICKEN)
                .build();
        
        foodRepository.save(food);
        System.out.println(foodRepository.findAll());
    }
}
