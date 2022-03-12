package com.mukcha.service;


import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import com.mukcha.controller.dto.CategoryDto;
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
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@SpringBootTest
public class ReviewServiceTest extends WithTest {

    Company company;
    Food food;
    User user;

    @BeforeEach
    void before() {
        prepareTest();
        this.company = companyTestHelper.createCompany("test company", "companyLogo");
        this.food = foodTestHelper.createFood("ReviewTestFood", company, Category.CHICKEN, null);
        this.user = userTestHelper.createUser("user@review.test", "reviewTestUser");
    }


    @Test // 22.3.5
    @DisplayName("1. 유저가 해당 음식에 점수와 코멘트를 매긴다.")
    void test_1() {
        Review review = reviewService.saveReview(Score.BEST, "comment", food, user);

        Review savedReview = reviewService.findReview(review.getReviewId()).orElseThrow(() -> 
            new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다.")
        );

        assertEquals("ReviewTestFood", savedReview.getFood().getName());
        assertEquals("reviewTestUser", savedReview.getUser().getNickname());
        assertEquals("comment", savedReview.getComment());
        assertEquals(Score.BEST, savedReview.getScore());
    }

    @Test // 22.3.5
    @DisplayName("2. 점수와 코멘트를 단 리뷰에 추가적으로 먹은날짜를 기록한다.")
    void test_2() {
        Review review = reviewTestHelper.createReview(food, user);
        reviewService.saveEatenDate("1991-12-14", food, user);

        Review savedReview = reviewService.findReview(review.getReviewId()).orElseThrow(() -> 
            new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다.")
        );

        assertEquals(LocalDate.of(1991, 12, 14), savedReview.getEatenDate());
    }

    @Test // 22.3.5
    @DisplayName("3. 리뷰의 점수, 코멘트, 먹은날짜를 수정한다.")
    void test_3() {
        Review review = reviewTestHelper.createReview(food, user);

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

    @Test // 22.3.5
    @DisplayName("4. 점수를 삭제하면 리뷰가 삭제된다.")
    void test_4() {
        reviewTestHelper.createReview(food, user);

        Food targetFood = foodService.findFood(food.getFoodId()).orElseThrow(() -> 
            new IllegalArgumentException("해당 메뉴를 찾을 수 없습니다.")
        );
        // 현재 로그인한 유저가 작성한 해당 음식의 리뷰를 찾는다.
        Review targetReview = reviewService.findReviewByFoodIdAndUserId(targetFood.getFoodId(), user.getUserId());
        // delete
        reviewService.deleteReview(targetReview);

        assertFalse(reviewService.findReview(targetReview.getReviewId()).isPresent());
    }

    @Test // 22.3.7
    @DisplayName("5. 해당 음식의 모든 리뷰를 찾는다")
    void test_5() {
        Food food1 = foodService.findFood(1L).get();
        Food food2 = foodService.findFood(2L).get();
        reviewTestHelper.createReview(food1, user);
        reviewTestHelper.createReview(food2, user);

        List<Review> reviews = reviewService.findAllReviewByFoodId(1L);

        assertEquals(1L, reviews.get(0).getFood().getFoodId());
    }

    @Test
    @DisplayName("6. 해당 음식의 모든 음식을 가장 최신순으로 페이징하여 가져온다")
    void test_6() {
        User user1 = userTestHelper.createUser("test2@review.test", "rt2");
        User user2 = userTestHelper.createUser("test3@review.test", "rt3");
        User user3 = userTestHelper.createUser("test4@review.test", "rt4");
        User user4 = userTestHelper.createUser("test5@review.test", "rt5");
        reviewTestHelper.createReview(food, user);
        reviewTestHelper.createReview(food, user1);
        reviewTestHelper.createReview(food, user2);
        reviewTestHelper.createReview(food, user3);
        reviewTestHelper.createReview(food, user4);

        Page<Review> reviewPage = reviewService.findAllByFoodIdOrderByCreatedAtDesc(food.getFoodId(), 1, 5);

        System.out.println(">>> reviewPage: "+reviewPage);
    }

    @Test
    @DisplayName("7. 해당 유저의 모든 리뷰 찾기")
    void test_7() {
        // set
        reviewTestHelper.createReview(food, user);
        reviewTestHelper.createReview(food, user);
        reviewTestHelper.createReview(food, user);
        reviewTestHelper.createReview(food, user);
        reviewTestHelper.createReview(food, user);
        
        List<Review> reviews = reviewService.findAllByUserId(user.getUserId());
        System.out.println(reviews);
    }

    @Test
    @DisplayName("8. 각 카테고리별 해당 유저의 모든 리뷰 수 가져오기")
    void test_8() {
        reviewTestHelper.createReview(food, user);
        reviewTestHelper.createReview(food, user);
        reviewTestHelper.createReview(food, user);
        reviewTestHelper.createReview(food, user);
        reviewTestHelper.createReview(food, user);
        
        CategoryDto categoryDto = reviewService.getReviewCountByCategoryAndUserId(user.getUserId());

        System.out.println(">>> chicken: "+categoryDto.getChickenReviewCount());
        System.out.println(">>> burger: "+categoryDto.getBurgerReviewCount());
        System.out.println(">>> pizza: "+categoryDto.getPizzaReviewCount());
        assertEquals(5, categoryDto.getChickenReviewCount());
        assertEquals(0, categoryDto.getBurgerReviewCount());
        assertEquals(0, categoryDto.getPizzaReviewCount());
    }





}
