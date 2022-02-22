package com.mukcha.service.helper;

import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.domain.User;
import com.mukcha.repository.ReviewRepository;
import com.mukcha.service.CompanyService;
import com.mukcha.service.FoodService;
import com.mukcha.service.ReviewService;
import com.mukcha.service.UserService;

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
