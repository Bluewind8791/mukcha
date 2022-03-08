package com.mukcha.service.helper;


import com.mukcha.repository.CompanyRepository;
import com.mukcha.repository.FoodRepository;
import com.mukcha.repository.ReviewRepository;
import com.mukcha.repository.UserRepository;
import com.mukcha.service.CompanyService;
import com.mukcha.service.FoodService;
import com.mukcha.service.ReviewService;
import com.mukcha.service.UserSecurityService;
import com.mukcha.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;


public class WithTest {
    
    @Autowired protected UserRepository userRepository;
    @Autowired protected ReviewRepository reviewRepository;
    @Autowired protected FoodRepository foodRepository;
    @Autowired protected CompanyRepository companyRepository;
    @Autowired protected PasswordEncoder passwordEncoder;
    
    protected UserService userService;
    protected ReviewService reviewService;
    protected CompanyService companyService;
    protected FoodService foodService;
    protected UserSecurityService userSecurityService;

    protected UserTestHelper userTestHelper;
    protected ReviewTestHelper reviewTestHelper;
    protected FoodTestHelper foodTestHelper;
    protected CompanyTestHelper companyTestHelper;

    protected void prepareTest() {
        // service
        this.userSecurityService = new UserSecurityService(userRepository);
        this.foodService = new FoodService(foodRepository, companyRepository, reviewRepository);
        this.companyService = new CompanyService(companyRepository);
        this.userService = new UserService(userRepository);
        this.reviewService = new ReviewService(reviewRepository);

        //test helper
        this.userTestHelper = new UserTestHelper(userService, passwordEncoder);
        this.reviewTestHelper = new ReviewTestHelper(reviewService);
        this.foodTestHelper = new FoodTestHelper(this.foodService);
        this.companyTestHelper = new CompanyTestHelper(this.companyService);
    }
}
