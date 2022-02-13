package com.mukcha.backend.service.helper;

import com.mukcha.backend.domain.Category;
import com.mukcha.backend.domain.Company;
import com.mukcha.backend.domain.Food;
import com.mukcha.backend.domain.User;
import com.mukcha.backend.repository.ReviewRepository;
import com.mukcha.backend.service.CompanyService;
import com.mukcha.backend.service.FoodService;
import com.mukcha.backend.service.ReviewService;
import com.mukcha.backend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class WithReviewTest {

    @Autowired protected ReviewRepository reviewRepository;

    @Autowired protected ReviewService reviewService;
    @Autowired protected FoodService foodService;
    @Autowired protected CompanyService companyService;
    @Autowired protected UserService userService;
    @Autowired protected PasswordEncoder passwordEncoder;

    protected FoodTestHelper foodTestHelper;
    protected CompanyTestHelper companyTestHelper;
    protected UserTestHelper userTestHelper;
    protected ReviewTestHelper reviewTestHelper;

    protected Food food;
    protected Company company;
    protected User user;

    protected void prepareReviewTest() {
        this.reviewRepository.deleteAll();

        this.reviewService = new ReviewService(reviewRepository);

        this.foodTestHelper = new FoodTestHelper(this.foodService);
        this.companyTestHelper = new CompanyTestHelper(this.companyService);
        this.userTestHelper = new UserTestHelper(userService, passwordEncoder);
        this.reviewTestHelper = new ReviewTestHelper();

        this.company = this.companyTestHelper.createCompany("test company", "companyLogo");
        this.food = this.foodTestHelper.createFood("test food", company, Category.CHICKEN, "menuImage");
    }



}
