package com.mukcha.backend.repository;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mukcha.backend.domain.Category;
import com.mukcha.backend.domain.Company;
import com.mukcha.backend.domain.Food;
import com.mukcha.backend.domain.Review;
import com.mukcha.backend.domain.User;




@SpringBootTest
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void test_1() {

        givenReview(givenUser(), givenFood());

        System.out.println(reviewRepository.findAll());
    }
    
    
    public void givenReview(User user, Food food) {
        Review review = new Review();
        review.setComment("존맛탱");
        review.setScore(4.5F);
        review.setEatenDate(LocalDate.now());
        review.setFood(food);
        review.setUser(user);
        reviewRepository.save(review);
    }

    public Food givenFood() {
        Food food = new Food();
        food.setName("원헌드RED");
        food.setCategory(Category.CHICKEN);
        food.setCompany(givenCompany());
        return foodRepository.save(food);
    }

    public User givenUser() {
        User user = new User();
        user.setUsername("ben");
        user.setEmail("ben@email.com");
        return userRepository.save(user);
    }

    public Company givenCompany() {
        Company company = new Company();
        company.setName("치킨플러스");
        return companyRepository.save(company);
    }
    

}
