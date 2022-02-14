package com.mukcha.backend.service;


import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import com.mukcha.backend.domain.Category;
import com.mukcha.backend.domain.Company;
import com.mukcha.backend.domain.Food;
import com.mukcha.backend.domain.Review;
import com.mukcha.backend.domain.Score;
import com.mukcha.backend.domain.User;
import com.mukcha.backend.repository.CompanyRepository;
import com.mukcha.backend.repository.FoodRepository;
import com.mukcha.backend.repository.ReviewRepository;
import com.mukcha.backend.service.helper.CompanyTestHelper;
import com.mukcha.backend.service.helper.FoodTestHelper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@SpringBootTest
public class ReviewServiceTest {

    @Autowired private ReviewRepository reviewRepository;
    @Autowired private CompanyRepository companyRepository;
    @Autowired private FoodRepository foodRepository;

    private CompanyTestHelper companyTestHelper;
    private FoodTestHelper foodTestHelper;
    private ReviewService reviewService;
    private CompanyService companyService;
    private FoodService foodService;
    Company company;
    Food food;
    User user;
    // Review review;

    @BeforeEach
    void before() {
        this.reviewRepository.deleteAll();

        this.companyService = new CompanyService(companyRepository);
        this.foodService = new FoodService(foodRepository);
        this.reviewService = new ReviewService(reviewRepository);
        this.companyTestHelper = new CompanyTestHelper(companyService);
        this.foodTestHelper = new FoodTestHelper(foodService);

        company = companyTestHelper.createCompany("test company", "companyLogo");
        food = foodTestHelper.createFood("food", company, Category.CHICKEN, null);
        user = User.builder().email("ben@test.com").password("1234").nickname("ben").build();
    }

    @DisplayName("1. 리뷰를 생성한다.")
    @Test
    void test_1() {
        Review review = new Review();
        review.setFood(food);
        review.setUser(user);
        review.setComment("존맛이에요");
        review.setEatenDate(LocalDate.now());
        review.setScore(Score.BEST);
        reviewRepository.save(review);

        Review savedReview = reviewService.findById(review.getReviewId());

        System.out.println(savedReview);
    }






/**
 * 1. 리뷰를 생성한다.
 * 2. 점수 없이 코멘트만 달 수 없다.
 * 3. 리뷰를 수정한다.
 * 
 */
}
