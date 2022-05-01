package com.mukcha.controller.helper;

import com.mukcha.domain.Authority;
import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.domain.User;
import com.mukcha.repository.CompanyRepository;
import com.mukcha.repository.FoodRepository;
import com.mukcha.repository.ReviewRepository;
import com.mukcha.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

public class WithControllerTest {

    @LocalServerPort
    protected int port;

    @Autowired protected TestRestTemplate restTemplate;
    @Autowired protected CompanyRepository companyRepository;
    @Autowired protected FoodRepository foodRepository;
    @Autowired protected UserRepository userRepository;
    @Autowired protected ReviewRepository reviewRepository;


    protected Company createCompany(String companyName, String companyLogo) {
        return companyRepository.save(Company.builder()
            .name(companyName)
            .image(companyLogo)
            .build()
        );
    }

    protected Food createFood(Company company, String foodName, String foodImage, Category category) {
        return foodRepository.save(Food.builder()
            .company(company)
            .name(foodName)
            .image(foodImage)
            .category(category)
            .build()
        );
    }

    protected User createUser(String email, String nickname) {
        return userRepository.save(User.builder()
            .nickname(nickname)
            .email(email)
            .authority(Authority.ADMIN)
            .enabled(true)
            .build()
        );
    }

}
