package com.mukcha.service.helper;


import com.mukcha.repository.CompanyRepository;
import com.mukcha.repository.FoodRepository;
import com.mukcha.repository.ReviewRepository;
import com.mukcha.repository.UserRepository;
import com.mukcha.service.CompanyService;
import com.mukcha.service.FoodService;
import com.mukcha.service.ReviewService;
import com.mukcha.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;


public class WithTest {
    
    @Autowired protected UserRepository userRepository;
    @Autowired protected ReviewRepository reviewRepository;
    @Autowired protected FoodRepository foodRepository;
    @Autowired protected CompanyRepository companyRepository;
    @Autowired protected AuthenticationManager authenticationManager;
    
    protected UserService userService;
    protected ReviewService reviewService;
    protected CompanyService companyService;
    protected FoodService foodService;

    protected UserTestHelper userTestHelper;
    protected ReviewTestHelper reviewTestHelper;
    protected FoodTestHelper foodTestHelper;
    protected CompanyTestHelper companyTestHelper;

    protected void prepareTest() {
        // service
        this.foodService = new FoodService(foodRepository, companyRepository, reviewRepository);
        this.companyService = new CompanyService(companyRepository, foodRepository);
        this.userService = new UserService(userRepository);
        this.reviewService = new ReviewService(reviewRepository, userService, foodService);

        //test helper
        this.userTestHelper = new UserTestHelper(this.userService);
        this.reviewTestHelper = new ReviewTestHelper(this.reviewRepository);
        this.foodTestHelper = new FoodTestHelper(this.foodRepository);
        this.companyTestHelper = new CompanyTestHelper(this.companyService);
    }
}
