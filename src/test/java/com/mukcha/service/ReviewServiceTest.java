package com.mukcha.service;


import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.domain.Review;
import com.mukcha.domain.Score;
import com.mukcha.domain.User;
import com.mukcha.repository.CompanyRepository;
import com.mukcha.repository.FoodRepository;
import com.mukcha.repository.ReviewRepository;
import com.mukcha.service.helper.CompanyTestHelper;
import com.mukcha.service.helper.FoodTestHelper;

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
    Review review = new Review();

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
        review.setFood(food);
        review.setUser(user);
    }

    @Test
    @DisplayName("1. 리뷰를 생성한다.")
    void test_1() {
        review.setScore(Score.BEST);
        reviewService.setReviewEatenDate(review, LocalDate.now());
        reviewService.setReviewComment(review, "존맛이에요");
        reviewRepository.save(review);

        Review savedReview = reviewService.findReview(review.getReviewId()).orElseThrow(() -> 
            new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다.")
        );

        assertEquals("food", savedReview.getFood().getName());
        assertEquals("ben", savedReview.getUser().getNickname());
        assertEquals("존맛이에요", savedReview.getComment());
        assertEquals(Score.BEST, savedReview.getScore());
        assertEquals(LocalDate.now(), savedReview.getEatenDate());
    }

    @Test
    @DisplayName("2. 점수를 매기지 않고 먹은날짜 설정과 코멘트를 달 수 없다.")
    void test_2() {
        assertThrows(
            IllegalArgumentException.class, 
            () -> reviewService.setReviewComment(review, "test comment"),
            "점수를 먼저 매겨주세요!");

        assertThrows(
            IllegalArgumentException.class, 
            () -> reviewService.setReviewEatenDate(review, LocalDate.now()),
            "점수를 먼저 매겨주세요!");
    }

    @Test
    @DisplayName("3. 리뷰의 점수, 코멘트, 먹은날짜를 수정한다.")
    void test_3() {
        review.setScore(Score.BEST);
        reviewService.setReviewEatenDate(review, LocalDate.now());
        reviewService.setReviewComment(review, "존맛이에요");
        reviewRepository.save(review);

        reviewService.editReviewScore(review, Score.GOOD);
        reviewService.editReviewComment(review, "괜찮았어요");
        reviewService.editReviewEatenDate(review, LocalDate.now().minusDays(1));

        Review savedReview = reviewService.findReview(review.getReviewId()).orElseThrow(() -> 
            new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다.")
        );
        assertEquals(Score.GOOD, savedReview.getScore());
        assertEquals("괜찮았어요", savedReview.getComment());
        assertEquals(LocalDate.now().minusDays(1), savedReview.getEatenDate());
    }

    @Test
    @DisplayName("4. 점수를 삭제하면 리뷰가 삭제된다.")
    void test_4() {
        reviewRepository.save(review);
        reviewService.deleteScore(review);

        assertTrue(reviewRepository.findAll().isEmpty());
    }





/**
 * 리뷰를 생성한다.
 * 점수를 매기지 않고 먹은날짜 설정과 코멘트를 달 수 없다.
 * 리뷰의 점수, 코멘트, 먹은날짜를 수정한다.
 * 점수를 삭제하면 리뷰가 삭제된다.
 */
}