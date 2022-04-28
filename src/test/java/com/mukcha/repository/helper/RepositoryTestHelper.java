package com.mukcha.repository.helper;

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

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RepositoryTestHelper {

    private final FoodRepository foodRepository;
    private final CompanyRepository companyRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public Food makeFood(String name, Company company, Category category, String image) {
        return Food.builder()
                .name(name)
                .company(company)
                .category(category)
                .image(image)
                .build();
    }

    public Food createFood(String name, Company company, Category category, String image) {
        return foodRepository.save(makeFood(name, company, category, image));
    }

    public Company makeCompany(String name, String imageUrl) {
        return Company.builder()
                .name(name)
                .image(imageUrl)
                .build();
    }

    public Company createCompany(String name, String imageUrl) {
        return companyRepository.save(makeCompany(name, imageUrl));
    }

    public Review makeReview(Food food, User user, String comment, Score score) {
        return Review.builder()
                .food(food)
                .user(user)
                .comment(comment)
                .score(score)
                .build();
    }

    public Review createReview(Food food, User user, String comment, Score score) {
        return reviewRepository.save(makeReview(food, user, comment, score));
    }

    public User makeUser(String email, String nickname) {
        return User.builder()
            .email(email)
            .nickname(nickname)
            .authority(Authority.USER)
            .enabled(true)
            .build();
    }

    public User createUser(String email, String nickname) {
        return userRepository.save(makeUser(email, nickname));
    }

}
