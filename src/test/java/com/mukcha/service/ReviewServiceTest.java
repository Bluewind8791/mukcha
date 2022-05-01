package com.mukcha.service;


import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import com.mukcha.controller.dto.CategoryCountResponseDto;
import com.mukcha.controller.dto.ReviewResponseDto;
import com.mukcha.controller.dto.ReviewSaveRequestDto;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@SpringBootTest
@ActiveProfiles("test")
public class ReviewServiceTest extends WithTest {

    Company company;
    Food food;
    User user;

    @BeforeEach
    void before() {
        prepareTest();
        this.company = companyTestHelper.createCompany("testCompany", "companyLogo");
        this.food = foodTestHelper.createFood("ReviewTestFood", company, Category.CHICKEN, null);
        this.user = userTestHelper.createUser("user@review.test", "reviewTestUser");
    }


    @Test
    @DisplayName("1. 유저가 해당 음식에 점수와 코멘트를 매긴다.")
    void test_1() {
        // given
        ReviewSaveRequestDto dto = new ReviewSaveRequestDto("testComment", "인생 메뉴에요!");
        Long reviewId = reviewService.saveReview(food.getFoodId(), user.getEmail(), dto);
        // when
        Review savedReview = reviewService.findReview(reviewId);
        System.out.println(savedReview);
        // then
        assertEquals("ReviewTestFood", savedReview.getFood().getName());
        assertEquals("reviewTestUser", savedReview.getUser().getNickname());
        assertEquals("testComment", savedReview.getComment());
        assertEquals(Score.BEST, savedReview.getScore());
    }

    @Test
    @DisplayName("2. 점수와 코멘트를 단 리뷰에 추가적으로 먹은날짜를 기록한다.")
    void testSaveEatenDate() {
        // given
        Review review = reviewTestHelper.createReview(food, user);
        // when
        reviewService.saveEatenDate(food.getFoodId(), user.getEmail(), "1991-12-14");
        // then
        Review savedReview = reviewService.findReview(review.getReviewId());
        assertEquals(LocalDate.of(1991, 12, 14), savedReview.getEatenDate());
    }

    @Test
    @DisplayName("3. 리뷰의 점수, 코멘트, 먹은날짜를 수정한다.")
    void test_3() {
        // given
        reviewTestHelper.createReview(food, user);
        reviewService.saveEatenDate(food.getFoodId(), user.getEmail(), "2000-01-01");
        // when
        ReviewSaveRequestDto dto = new ReviewSaveRequestDto("testComment", "인생 메뉴에요!");
        Long reviewId = reviewService.saveReview(food.getFoodId(), user.getEmail(), dto);
        reviewService.saveEatenDate(food.getFoodId(), user.getEmail(), "2022-05-01");
        // then
        Review savedReview = reviewService.findReview(reviewId);
        System.out.println(savedReview);
        assertEquals("ReviewTestFood", savedReview.getFood().getName());
        assertEquals("reviewTestUser", savedReview.getUser().getNickname());
        assertEquals("testComment", savedReview.getComment());
        assertEquals(Score.BEST, savedReview.getScore());
        assertEquals(LocalDate.of(2022, 05, 01), savedReview.getEatenDate());
    }

    @Test
    @DisplayName("4. 사용자가 해당 메뉴에 달았던 리뷰를 삭제한다.")
    void test_4() {
        // given
        Review review = reviewTestHelper.createReview(food, user);
        // when
        reviewService.deleteReview(food.getFoodId(), user.getEmail());
        // then
        assertThrows(IllegalArgumentException.class, () -> reviewService.findReview(review.getReviewId()));
    }

    @Test
    @DisplayName("5. 해당 음식의 모든 리뷰를 찾는다")
    void test_5() {
        // given
        reviewTestHelper.createReview(food, user);
        User user2 = userTestHelper.createUser("email2", "nickname2");
        reviewTestHelper.createReview(food, user2);
        User user3 = userTestHelper.createUser("email3", "nickname3");
        reviewTestHelper.createReview(food, user3);
        // when
        List<ReviewResponseDto> reviews = reviewService.findAllByFoodId(food.getFoodId());
        // then
        assertEquals(3, reviews.size());
    }

    @Test
    @DisplayName("6. 해당 음식의 모든 음식을 가장 최신순으로 페이징하여 가져온다")
    void testfindAllByFoodIdOrderByCreatedAtDesc() {
        // given
        User user1 = userTestHelper.createUser("test2@review.test", "rt2");
        User user2 = userTestHelper.createUser("test3@review.test", "rt3");
        User user3 = userTestHelper.createUser("test4@review.test", "rt4");
        User user4 = userTestHelper.createUser("test5@review.test", "rt5");
        reviewTestHelper.createReview(food, user);
        reviewTestHelper.createReview(food, user1);
        reviewTestHelper.createReview(food, user2);
        reviewTestHelper.createReview(food, user3);
        reviewTestHelper.createReview(food, user4);
        // when
        Page<ReviewResponseDto> reviewPage = reviewService.findAllByFoodIdOrderByCreatedAtDesc(food.getFoodId(), 1, 5);
        // then
        System.out.println(">>> reviewPage: "+reviewPage);
    }

    @Test
    @DisplayName("8. 각 카테고리별 해당 유저의 모든 리뷰 수 가져오기")
    void test_8() {
        reviewTestHelper.createReview(food, user);
        reviewTestHelper.createReview(food, user);
        reviewTestHelper.createReview(food, user);
        reviewTestHelper.createReview(food, user);
        reviewTestHelper.createReview(food, user);
        
        CategoryCountResponseDto categoryDto = reviewService.getCountByCategoryAndUserId(user.getUserId());

        System.out.println(">>> chicken: "+categoryDto.getChickenReviewCount());
        System.out.println(">>> burger: "+categoryDto.getBurgerReviewCount());
        System.out.println(">>> pizza: "+categoryDto.getPizzaReviewCount());
        assertEquals(5, categoryDto.getChickenReviewCount());
        assertEquals(0, categoryDto.getBurgerReviewCount());
        assertEquals(0, categoryDto.getPizzaReviewCount());
    }

    @Test
    @DisplayName("해당 유저가 해당 메뉴의 리뷰를 적었는지 확인")
    void testIsUserWriteReviewOnFood() {
        // given
        reviewTestHelper.createReview(food, user);
        // when
        boolean result = reviewService.isUserWriteReviewOnFood(food.getFoodId(), user.getUserId());
        // then
        assertEquals(true, result);
    }




}
