package com.mukcha.repository.helper;

import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.domain.Review;
import com.mukcha.domain.User;
import com.mukcha.repository.CompanyRepository;
import com.mukcha.repository.FoodRepository;
import com.mukcha.repository.ReviewRepository;
import com.mukcha.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

public class WithRepositoryTest {

    @Autowired protected FoodRepository foodRepository;
    @Autowired protected CompanyRepository companyRepository;
    @Autowired protected ReviewRepository reviewRepository;
    @Autowired protected UserRepository userRepository;

    protected RepositoryTestHelper repositoryTestHelper;
    protected Food food;
    protected Company company;
    protected Review review;
    protected User user;

    protected void prepareTest() {
        //test helper
        this.repositoryTestHelper = new RepositoryTestHelper(foodRepository, companyRepository, reviewRepository, userRepository);
        // test entity
        this.company = repositoryTestHelper.createCompany("testCompany", "testCompanyLogo");
        this.food = repositoryTestHelper.createFood("testFood", company, Category.CHICKEN, "testFoodImage");
        this.user = repositoryTestHelper.createUser("test@email.com", "testUsername");
    }

}
