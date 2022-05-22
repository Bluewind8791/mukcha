package com.mukcha.controller.helper;

import com.mukcha.controller.dto.SessionUserResponseDto;
import com.mukcha.domain.Authority;
import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.domain.Review;
import com.mukcha.domain.Score;
import com.mukcha.domain.User;
import com.mukcha.repository.CompanyRepository;
import com.mukcha.repository.FoodRepository;
import com.mukcha.repository.ReviewRepository;
import com.mukcha.repository.UserRepository;
import com.mukcha.service.FoodService;
import com.mukcha.service.ReviewService;
import com.mukcha.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;


public class WithMockMvcTest {

    @Autowired protected WebApplicationContext context;
    @Autowired protected FoodRepository foodRepository;
    @Autowired protected CompanyRepository companyRepository;
    @Autowired protected UserRepository userRepository;
    @Autowired protected ReviewRepository reviewRepository;
    
    protected UserService userService;
    protected ReviewService reviewService;
    protected FoodService foodService;
    protected MockMvc mvc;
    protected SessionUserResponseDto userResDto;

    @LocalServerPort
    protected int port;

    protected void prepareTest() {
        // service
        this.userService = new UserService(userRepository);
        this.foodService = new FoodService(foodRepository, companyRepository, reviewRepository);
        this.reviewService = new ReviewService(reviewRepository, userService, foodService);
        // this.companyService = new CompanyService(companyRepository, foodRepository);
        companyRepository.deleteAll();
        reviewRepository.deleteAll();
        foodRepository.deleteAll();
        userRepository.deleteAll();
    }

    
    public Company createCompany(String companyName, String companyLogo) {
        return companyRepository.save(Company.builder()
            .name(companyName)
            .image(companyLogo)
            .build()
        );
    }

    public Food createFood(Company company, String foodName, String foodImage, Category category) {
        return foodRepository.save(Food.builder()
            .company(company)
            .name(foodName)
            .image(foodImage)
            .category(category)
            .build()
        );
    }

    public User createUser(String email, String nickname) {
        return userRepository.save(User.builder()
            .nickname(nickname)
            .email(email)
            .authority(Authority.USER)
            .enabled(true)
            .build()
        );
    }

    public User createAdminUser(String email, String nickname) {
        return userRepository.save(User.builder()
            .nickname(nickname)
            .email(email)
            .authority(Authority.ADMIN)
            .enabled(true)
            .build()
        );
    }

    public Review createReview(User user, Food food, String comment, Score score) {
        return reviewRepository.save(Review.builder()
            .food(food)
            .user(user)
            .comment(comment)
            .score(score)
            .build()
        );
    }


}
