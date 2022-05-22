package com.mukcha.controller.helper;

import com.mukcha.controller.dto.SessionUserResponseDto;
import com.mukcha.domain.Authority;
import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.domain.User;
import com.mukcha.repository.CompanyRepository;
import com.mukcha.repository.FoodRepository;
import com.mukcha.repository.ReviewRepository;
import com.mukcha.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class MockMvcTestHelper {

    @Autowired protected WebApplicationContext context;
    @Autowired protected FoodRepository foodRepository;
    @Autowired protected CompanyRepository companyRepository;
    @Autowired protected UserRepository userRepository;
    @Autowired protected ReviewRepository reviewRepository;

    protected MockMvc mvc;
    protected User user = User.builder()
            .email("admin@test.com")
            .nickname("ben-admin")
            .authority(Authority.ADMIN)
            .enabled(true)
            .build();
    protected SessionUserResponseDto userResDto = new SessionUserResponseDto(user);

    @LocalServerPort
    protected int port;

    @BeforeEach
    protected void before() {
        mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
    }

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
