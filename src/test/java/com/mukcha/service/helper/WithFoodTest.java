package com.mukcha.service.helper;

import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.domain.Review;
import com.mukcha.domain.User;
import com.mukcha.repository.CompanyRepository;
import com.mukcha.repository.FoodRepository;
import com.mukcha.repository.ReviewRepository;
import com.mukcha.repository.UserRepository;
import com.mukcha.service.CompanyService;
import com.mukcha.service.FoodService;
import com.mukcha.service.ReviewService;
import com.mukcha.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

public class WithFoodTest {
    
    @Autowired protected FoodRepository foodRepository;
    @Autowired protected CompanyRepository companyRepository;
    @Autowired protected ReviewRepository reviewRepository;
    @Autowired protected UserRepository userRepository;

    @Autowired protected CompanyService companyService;
    @Autowired protected FoodService foodService;
    @Autowired protected UserService userService;
    @Autowired protected ReviewService reviewService;

    protected FoodTestHelper foodTestHelper;
    protected CompanyTestHelper companyTestHelper;
    protected UserTestHelper userTestHelper;
    protected ReviewTestHelper reviewTestHelper;

    protected Food food;
    protected Company company;
    protected Review review;
    protected User user;


    protected void prepareFoodTest() {
        // service
        this.foodService = new FoodService(foodRepository, companyService, reviewService);
        this.companyService = new CompanyService(companyRepository);
        this.userService = new UserService(userRepository);
        this.reviewService = new ReviewService(reviewRepository);
        // test helper
        this.foodTestHelper = new FoodTestHelper(this.foodService);
        this.companyTestHelper = new CompanyTestHelper(this.companyService);
        this.userTestHelper = new UserTestHelper(userService, NoOpPasswordEncoder.getInstance());
        this.reviewTestHelper = new ReviewTestHelper(reviewService);
        // domain
        this.company = this.companyTestHelper.createCompany("testCompany", "companyLogo");
        this.food = this.foodTestHelper.createFood("testFood", company, Category.CHICKEN, "menuImage");
        this.user = this.userTestHelper.createUser("testuser@test.com", "testuser");
        this.review = this.reviewTestHelper.createReview(food, user);
    }
}
