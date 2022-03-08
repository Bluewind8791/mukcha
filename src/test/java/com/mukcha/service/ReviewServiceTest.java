package com.mukcha.service;


import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

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
import com.mukcha.service.helper.ReviewTestHelper;
import com.mukcha.service.helper.UserTestHelper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@SpringBootTest
public class ReviewServiceTest {

    @Autowired private ReviewRepository reviewRepository;
    @Autowired private CompanyRepository companyRepository;
    @Autowired private FoodRepository foodRepository;

    @Autowired private ReviewService reviewService;
    @Autowired private CompanyService companyService;
    @Autowired private FoodService foodService;
    @Autowired private UserService userService;

    private CompanyTestHelper companyTestHelper;
    private FoodTestHelper foodTestHelper;
    private UserTestHelper userTestHelper;
    private ReviewTestHelper reviewTestHelper;

    Company company;
    Food food;
    User user;


    @BeforeEach
    void before() {
        this.companyService = new CompanyService(companyRepository);
        this.foodService = new FoodService(foodRepository, companyService, reviewService);
        this.reviewService = new ReviewService(reviewRepository);

        this.userTestHelper = new UserTestHelper(userService, NoOpPasswordEncoder.getInstance());
        this.companyTestHelper = new CompanyTestHelper(companyService);
        this.foodTestHelper = new FoodTestHelper(foodService);
        this.reviewTestHelper = new ReviewTestHelper(reviewService);

        this.company = companyTestHelper.createCompany("test company", "companyLogo");
        this.food = foodTestHelper.createFood("ReviewTestFood", company, Category.CHICKEN, null);
        this.user = userTestHelper.createUser("user@review.test", "reviewTestUser");
    }


    @Test // 22.3.5
    @DisplayName("1. 리뷰를 생성한다.")
    void test_1() {
        Review review = new Review();
        review.setFood(food);
        review.setUser(user);
        review.setScore(Score.BEST);
        reviewService.setReviewEatenDate(review, LocalDate.now());
        reviewService.setReviewComment(review, "ReviewTestComment");
        reviewService.save(review);

        Review savedReview = reviewService.findReview(review.getReviewId()).orElseThrow(() -> 
            new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다.")
        );

        assertEquals("ReviewTestFood", savedReview.getFood().getName());
        assertEquals("reviewTestUser", savedReview.getUser().getNickname());
        assertEquals("ReviewTestComment", savedReview.getComment());
        assertEquals(Score.BEST, savedReview.getScore());
        assertEquals(LocalDate.now(), savedReview.getEatenDate());
    }

    @Test // 22.3.5
    @DisplayName("2. 점수를 매기지 않고 먹은날짜 설정과 코멘트를 달 수 없다.")
    void test_2() {
        Review review = new Review();
        review.setFood(food);
        review.setUser(user);

        assertThrows(
            IllegalArgumentException.class, 
            () -> reviewService.setReviewComment(review, "test comment"),
            "점수를 먼저 매겨주세요!");

        assertThrows(
            IllegalArgumentException.class, 
            () -> reviewService.setReviewEatenDate(review, LocalDate.now()),
            "점수를 먼저 매겨주세요!");
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
    @DisplayName("5. findReviewByFoodId")
    void test_5() {
        Food food1 = foodService.findFood(1L).get();
        Food food2 = foodService.findFood(2L).get();
        reviewTestHelper.createReview(food1, user);
        reviewTestHelper.createReview(food2, user);

        List<Review> reviews = reviewService.findAllReviewByFoodId(1L);

        assertEquals(1L, reviews.get(0).getFood().getFoodId());
    }







}
