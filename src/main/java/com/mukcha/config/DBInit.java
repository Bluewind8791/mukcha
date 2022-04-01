package com.mukcha.config;

import java.util.Optional;

import com.mukcha.domain.Authority;
import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.domain.Gender;
import com.mukcha.domain.User;
import com.mukcha.service.CompanyService;
import com.mukcha.service.FoodService;
import com.mukcha.service.UserService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

// for test init DB
@Component
@RequiredArgsConstructor
public class DBInit implements CommandLineRunner {

    private final UserService userService;
    private final CompanyService companyService;
    private final FoodService foodService;

    @Override
    public void run(String... args) throws Exception {

        // init admin user
        userService.findByEmail("admin@test.com").or(() ->{
            User admin = User.builder()
                            .email("admin@test.com")
                            .nickname("admin_ben")
                            .authority(Authority.ADMIN)
                            .gender(Gender.MALE)
                            .birthYear("1991")
                            .build();
            userService.signUp(admin);
            return Optional.of(userService.signUp(admin));
        });
        userService.findByEmail("castus1214@naver.com").or(() ->{
            User admin = User.builder()
                            .email("castus1214@naver.com")
                            .nickname("찡빈")
                            .authority(Authority.ADMIN)
                            .gender(Gender.MALE)
                            .birthYear("1991")
                            .build();
            userService.signUp(admin);
            return Optional.of(userService.signUp(admin));
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
        companyService.findByName("BHC").or(() -> {
            Company company = Company.builder()
                                .name("BHC")
                                .image("https://www.bhc.co.kr/images/index/img_bottom_brand1.jpg")
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
        foodService.findByName("하바네로 포테킹 후라이드").or(() -> {
            Food food = Food.builder()
                            .name("하바네로 포테킹 후라이드")
                            .category(Category.CHICKEN)
                            .image("http://www.bhc.co.kr/upload/bhc/menu/%ED%95%98%EB%B0%94%EB%84%A4%EB%A1%9C%20%ED%8F%AC%ED%85%8C%ED%82%B9_410x271(0).png")
                            .company(companyService.findByName("BHC").get())
                            .build()
            ;
            foodService.save(food);
            return Optional.of(foodService.save(food));
        });
        foodService.findByName("뿌링클").or(() -> {
            Food food = Food.builder()
                            .name("뿌링클")
                            .category(Category.CHICKEN)
                            .image("http://www.bhc.co.kr//upload/bhc/menu//BB(0).jpg")
                            .company(companyService.findByName("BHC").get())
                            .build()
            ;
            foodService.save(food);
            return Optional.of(foodService.save(food));
        });
        foodService.findByName("뿌링클HOT").or(() -> {
            Food food = Food.builder()
                            .name("뿌링클HOT")
                            .category(Category.CHICKEN)
                            .image("http://www.bhc.co.kr//upload/bhc/menu//BBUHOT.jpg")
                            .company(companyService.findByName("BHC").get())
                            .build()
            ;
            foodService.save(food);
            return Optional.of(foodService.save(food));
        });




    } // end of run method

}
