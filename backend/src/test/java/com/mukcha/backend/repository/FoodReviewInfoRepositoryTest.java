package com.mukcha.backend.repository;

import com.mukcha.backend.domain.Category;
import com.mukcha.backend.domain.Food;
import com.mukcha.backend.domain.FoodReviewInfo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@SpringBootTest
public class FoodReviewInfoRepositoryTest {

    @Autowired
    private FoodReviewInfoRepository foodReviewInfoRepository;

    @Autowired
    private FoodRepository foodRepository;

    @DisplayName("1. Food Review Info Test")
    @Test
    void test_1() {
        FoodReviewInfo foodReviewInfo = new FoodReviewInfo();
        foodReviewInfo.setFood(givenFood());
        foodReviewInfo.setAverageReviewScore(4.5F);
        foodReviewInfoRepository.save(foodReviewInfo);

        System.out.println(foodReviewInfoRepository.findAll());
    }

    private Food givenFood() {
        Food food = new Food();
        food.setName("원헌드RED");
        food.setCategory(Category.CHICKEN);

        return foodRepository.save(food);
    }



}
