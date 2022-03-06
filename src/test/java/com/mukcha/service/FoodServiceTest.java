package com.mukcha.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.mukcha.domain.Category;
import com.mukcha.domain.Food;
import com.mukcha.domain.Score;
import com.mukcha.service.helper.WithFoodTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@SpringBootTest
public class FoodServiceTest extends WithFoodTest {

    @BeforeEach
    void before() {
        prepareFoodTest();
    }

    @Test // 22.3.6
    @DisplayName("1. 음식을 생성한다")
    void test_1() {
        foodTestHelper.createFood("FoodTest1", company, Category.HAMBURGER, "image");

        Food savedFood = foodService.findByName("FoodTest1").get();
        assertEquals("FoodTest1", savedFood.getName());
        assertEquals(company, savedFood.getCompany());
        assertEquals(Category.HAMBURGER, savedFood.getCategory());
        assertEquals("image", savedFood.getImage());
    }

    @Test // 22.03.03
    @DisplayName("2. 음식 이름, 회사, 이미지, 카테고리를 수정한다.")
    void test_2() {
        foodService.editFoodName(food.getFoodId(), "test2 food");
        foodService.editFoodImage(food.getFoodId(), "imageUrl2");
        foodService.editFoodCategory(food.getFoodId(), Category.PIZZA);

        Food savedFood = foodService.findFood(food.getFoodId()).get();
        
        assertEquals("test2 food", savedFood.getName());
        assertEquals("imageUrl2", savedFood.getImage());
        assertEquals(Category.PIZZA, savedFood.getCategory());
    }

    @Test // 22.3.6
    @DisplayName("4. 카테고리 별 음식을 가져온다")
    void test_4() {
        // DB init 5 + test helper 1 = 6
        List<Food> list = foodService.findAllByCategory(Category.CHICKEN);
        assertEquals(6, list.size());

        foodTestHelper.createFood("test2 food", null, Category.CHICKEN, null);
        list = foodService.findAllByCategory(Category.CHICKEN);
        assertEquals(7, list.size());
    }

    @Test // 22.3.6
    @DisplayName("5. 음식의 평균 별점을 가져온다")
    void test_5() {
        reviewTestHelper.createReviewWithScore(food, user, Score.BAD);
        Food savedFood = foodService.findByName("testFood").get();
        // (4+1)/2
        assertEquals(2.5F, savedFood.getAverageScore());
    }


}
