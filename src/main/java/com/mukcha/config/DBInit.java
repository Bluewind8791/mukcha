package com.mukcha.config;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import com.mukcha.domain.Authority;
import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.domain.Gender;
import com.mukcha.domain.Review;
import com.mukcha.domain.Score;
import com.mukcha.domain.User;
import com.mukcha.service.CompanyService;
import com.mukcha.service.FoodService;
import com.mukcha.service.ReviewService;
import com.mukcha.service.UserService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

// for test init DB
@Component
@RequiredArgsConstructor
public class DBInit implements CommandLineRunner {

    private final UserService userService;
    private final CompanyService companyService;
    private final FoodService foodService;
    private final ReviewService reviewService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // init admin user
        userService.findByEmail("admin@test.com").or(() ->{
            User admin = User.builder()
                            .email("admin@test.com")
                            .nickname("admin_ben")
                            .password(passwordEncoder.encode("qwe1"))
                            .enabled(true)
                            .build();
            userService.save(admin);
            admin.setAuthorities(Set.of(
                new Authority(admin.getUserId(), Authority.ROLE_USER),
                new Authority(admin.getUserId(), Authority.ROLE_ADMIN)
            ));
            return Optional.of(userService.save(admin));
        });
        // init user
        userService.findByEmail("user@test.com").or(() ->{
            User user = User.builder()
                            .email("user@test.com")
                            .nickname("test_user")
                            .password(passwordEncoder.encode("qwe1"))
                            .gender(Gender.MALE)
                            .birthday(LocalDate.of(1991, 12, 14))
                            .enabled(true)
                            .build();
            userService.save(user);
            user.setAuthorities(Set.of(
                new Authority(user.getUserId(), Authority.ROLE_USER)
            ));
            return Optional.of(userService.save(user));
        });

        // init company
        companyService.findByName("치킨플러스").or(() -> {
            Company company = Company.builder()
                                .name("치킨플러스")
                                .image("http://www.chickenplus.co.kr/images/main/logo.png")
                                .build()
            ;
            companyService.save(company);
            return Optional.of(companyService.save(company));
        });

        // init menus
        foodService.findByName("NO1.포테이토닭토닭").or(() -> {
            Food food = Food.builder()
                            .name("NO1.포테이토닭토닭")
                            .category(Category.CHICKEN)
                            .image("http://www.chickenplus.co.kr/uploads/products/20220228_p_1.png")
                            .company(companyService.findByName("치킨플러스").get())
                            .build()
            ;
            foodService.save(food);
            return Optional.of(foodService.save(food));
        });
        foodService.findByName("아빠의제주깜슐랭").or(() -> {
            Food food = Food.builder()
                            .name("아빠의제주깜슐랭")
                            .category(Category.CHICKEN)
                            .image("http://www.chickenplus.co.kr/uploads/products/%EC%A0%9C%EC%A3%BC%EA%B9%9C%EC%8A%90%EB%9E%AD%EC%B9%98%ED%82%A8.png")
                            .company(companyService.findByName("치킨플러스").get())
                            .build()
            ;
            foodService.save(food);
            return Optional.of(foodService.save(food));
        });
        foodService.findByName("원헌드RED").or(() -> {
            Food food = Food.builder()
                            .name("원헌드RED")
                            .category(Category.CHICKEN)
                            .image("http://www.chickenplus.co.kr/uploads/products/%EC%9B%90%ED%97%8C%EB%93%9C%EB%A0%88%EB%93%9C%EC%B9%98%ED%82%A8.png")
                            .company(companyService.findByName("치킨플러스").get())
                            .build()
            ;
            foodService.save(food);
            return Optional.of(foodService.save(food));
        });
        foodService.findByName("신비아파트치플세트").or(() -> {
            Food food = Food.builder()
                            .name("신비아파트치플세트")
                            .category(Category.CHICKEN)
                            .image("http://www.chickenplus.co.kr/uploads/products/%EC%8B%A0%EB%B9%84%EC%95%84%ED%8C%8C%ED%8A%B8%EC%B9%98%ED%82%A8.png")
                            .company(companyService.findByName("치킨플러스").get())
                            .build()
            ;
            foodService.save(food);
            return Optional.of(foodService.save(food));
        });
        foodService.findByName("치플백립").or(() -> {
            Food food = Food.builder()
                            .name("치플백립")
                            .category(Category.CHICKEN)
                            .image("http://www.chickenplus.co.kr/uploads/products/%EB%B0%B1%EB%A6%BD%EC%BD%A4%EB%B3%B4.png")
                            .company(companyService.findByName("치킨플러스").get())
                            .build()
            ;
            foodService.save(food);
            return Optional.of(foodService.save(food));
        });

        // init review
        reviewService.findReview(1L).or(() -> {
            Review review = new Review();
            review.setFood(foodService.findFood(1L).get());
            review.setUser(userService.findByEmail("user@test.com").get());
            review.setScore(Score.GOOD);
            review.setComment("WOW!");
            review.setEatenDate(LocalDate.now());
            reviewService.save(review);
            return Optional.of(reviewService.save(review));
        });


    } // end of run method


}
