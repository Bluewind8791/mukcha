package com.mukcha.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.repository.CompanyRepository;
import com.mukcha.repository.FoodRepository;
import com.mukcha.repository.ReviewRepository;
import com.mukcha.service.helper.CompanyTestHelper;
import com.mukcha.service.helper.FoodTestHelper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@SpringBootTest
public class CompanyServiceTest {

    @Autowired private CompanyRepository companyRepository;
    @Autowired private FoodRepository foodRepository;
    @Autowired private ReviewRepository reviewRepository;

    private CompanyTestHelper companyTestHelper;
    private FoodTestHelper foodTestHelper;
    private CompanyService companyService;
    private FoodService foodService;
    private ReviewService reviewService;
    Company company;
    Food food1;
    Food food2;

    @BeforeEach
    void before() {
        this.companyService = new CompanyService(companyRepository);
        this.reviewService = new ReviewService(reviewRepository);
        this.foodService = new FoodService(foodRepository, companyService, reviewService);

        this.companyTestHelper = new CompanyTestHelper(companyService);
        this.foodTestHelper = new FoodTestHelper(foodService);
        company = companyTestHelper.createCompany("test company", "companyLogo");
        food1 = foodTestHelper.createFood("food1", company, Category.CHICKEN, null);
        food2 = foodTestHelper.createFood("food2", company, Category.PIZZA, null);
    }

    @Test // 22.3.6
    @DisplayName("1. 회사를 생성한다.")
    void test_1() {
        // Company company = companyTestHelper.createCompany("test company", "companyLogo");
        Company savedCompany = companyService.findCompany(company.getCompanyId()).orElseThrow(() -> 
            new IllegalArgumentException("해당 회사를 찾을 수 없습니다.")
        );
        companyTestHelper.assertCompany(savedCompany, "test company", "companyLogo");
    }

    @Test // 22.3.6
    @DisplayName("2. 회사의 음식/메뉴를 추가한다.")
    void test_2() {
        companyService.companyAddFood(company.getCompanyId(), food1);
        assertEquals(1, company.getFoods().size());
        companyService.companyAddFood(company.getCompanyId(), food2);
        assertEquals(2, company.getFoods().size());

        assertEquals("food1", company.getFoods().get(0).getName());
        assertEquals("food2", company.getFoods().get(1).getName());
    }

    @Test // 22.3.6
    @DisplayName("3. 회사의 음식/메뉴는 중복하여 들어가지 않는다.")
    void test_3() {
        companyService.companyAddFood(company.getCompanyId(), food1);
        companyService.companyAddFood(company.getCompanyId(), food1);
        assertEquals(1, company.getFoods().size());
        assertEquals("food1", company.getFoods().iterator().next().getName());
    }

    @Test // 22.3.6
    @DisplayName("4. 회사의 이름, 이미지URL를 수정한다.")
    void test_4() {
        companyService.updateCompanyName(company.getCompanyId(), "ChickenPlus");
        companyService.updateCompanyImage(company.getCompanyId(), "NewCompanyLogo");
        Company savedCompany = companyService.findCompany(company.getCompanyId()).orElseThrow(() -> 
            new IllegalArgumentException("해당 회사를 찾을 수 없습니다.")
        );
        assertEquals("ChickenPlus", savedCompany.getName());
        assertEquals("NewCompanyLogo", savedCompany.getImage());
    }

    @Test // 22.3.6
    @DisplayName("5. 회사의 음식을 삭제한다.")
    void test_5() {
        companyService.companyAddFood(company.getCompanyId(), food1);
        companyService.companyAddFood(company.getCompanyId(), food2);

        companyService.CompanyRemoveFood(company.getCompanyId(), food1);
        foodService.FoodRemoveCompany(food1.getFoodId());

        Company savedCompany = companyService.findCompany(company.getCompanyId()).orElseThrow(() -> 
            new IllegalArgumentException("해당 회사를 찾을 수 없습니다.")
        );
        assertEquals(1, savedCompany.getFoods().size());
        assertEquals("food2", savedCompany.getFoods().iterator().next().getName());

        System.out.println(">>> "+savedCompany.getFoods());
    }



}