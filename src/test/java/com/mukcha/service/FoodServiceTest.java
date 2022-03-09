package com.mukcha.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.mukcha.controller.dto.FoodDto;
import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.domain.Review;
import com.mukcha.domain.Score;
import com.mukcha.domain.User;
import com.mukcha.service.helper.WithTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@SpringBootTest
public class FoodServiceTest extends WithTest {

    Food food;
    Review review;
    User user;
    Company company;
    Company company1;

    @BeforeEach
    void before() {
        prepareTest();
        // domain
        this.company = this.companyTestHelper.createCompany("testCompany", "companyLogo");
        this.company1 = this.companyTestHelper.createCompany("testCompany2", "companyLogo2");
        this.food = this.foodTestHelper.createFood("testFood", company, Category.CHICKEN, "menuImage");
        this.user = this.userTestHelper.createUser("testuser@test.com", "testuser");
        this.review = this.reviewTestHelper.createReview(food, user);
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
        foodService.editFoodCompany(food.getFoodId(), "testCompany2");

        Food savedFood = foodService.findFood(food.getFoodId()).get();

        assertEquals("test2 food", savedFood.getName());
        assertEquals("imageUrl2", savedFood.getImage());
        assertEquals(Category.PIZZA, savedFood.getCategory());
        assertEquals("testCompany2", savedFood.getCompany().getName());
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
        reviewTestHelper.createReviewWithScore(food, user, Score.BAD); // 1
        reviewTestHelper.createReviewWithScore(food, user, Score.GOOD); // 4
        // System.out.println(">>> "+reviewService.findAllReviewByFoodId(
        //     food.getFoodId()).stream().map(
        //     r -> r.getScore().value
        // ).collect(Collectors.toList()));

        Food savedFood = foodService.findByName(food.getName()).get();
        double averageScore = foodService.getAverageScoreByFoodId(savedFood.getFoodId());
        assertEquals(2.0F, averageScore); // (1+1+4)/3
    }


    @Test
    @DisplayName("6. 평균별점을 기준으로 상위 10개의 메뉴를 가져온다")
    void test_6() {
        Food food1 = foodTestHelper.createFood("ttest1", company, Category.HAMBURGER, null);
        Food food2 = foodTestHelper.createFood("ttest2", company, Category.HAMBURGER, null);
        Food food3 = foodTestHelper.createFood("ttest3", company, Category.HAMBURGER, null);
        Food food4 = foodTestHelper.createFood("ttest4", company, Category.HAMBURGER, null);
        Food food5 = foodTestHelper.createFood("ttest5", company, Category.HAMBURGER, null);
        reviewTestHelper.createReviewWithScore(food1, user, Score.GOOD);
        reviewTestHelper.createReviewWithScore(food2, user, Score.GOOD);
        reviewTestHelper.createReviewWithScore(food3, user, Score.GOOD);
        reviewTestHelper.createReviewWithScore(food4, user, Score.GOOD);
        reviewTestHelper.createReviewWithScore(food5, user, Score.GOOD);

        // get
        List<FoodDto> foodDtos = foodService.findAllWithAverageScore();

        List<Double> before = foodDtos.stream().map(f -> f.getAverageScore()).collect(Collectors.toList());
        System.out.println(">>> before: "+before);

        // sort
        Collections.sort(
            foodDtos, Comparator.comparing(FoodDto::getAverageScore).reversed()
        );
        // get top 10
        foodDtos = foodDtos.stream().limit(10).collect(Collectors.toList());

        // assert
        List<Double> after = foodDtos.stream().map(f -> f.getAverageScore()).collect(Collectors.toList());
        System.out.println(">>> after:  "+after);
        assertEquals(10, foodDtos.size());
    }


}
