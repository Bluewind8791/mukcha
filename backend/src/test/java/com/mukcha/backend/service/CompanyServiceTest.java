package com.mukcha.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mukcha.backend.domain.Category;
import com.mukcha.backend.domain.Company;
import com.mukcha.backend.domain.Food;
import com.mukcha.backend.repository.CompanyRepository;
import com.mukcha.backend.repository.FoodRepository;
import com.mukcha.backend.service.helper.CompanyTestHelper;
import com.mukcha.backend.service.helper.FoodTestHelper;

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

    private CompanyTestHelper companyTestHelper;
    private CompanyService companyService;
    private FoodService foodService;
    private FoodTestHelper foodTestHelper;
    Company company;
    Food food1;
    Food food2;

    @BeforeEach
    void before() {
        this.companyRepository.deleteAll();
        this.companyService = new CompanyService(companyRepository);
        this.foodService = new FoodService(foodRepository);
        this.companyTestHelper = new CompanyTestHelper(companyService);
        this.foodTestHelper = new FoodTestHelper(foodService);
        company = companyTestHelper.createCompany("test company", "companyLogo");
        food1 = foodTestHelper.createFood("food1", company, Category.CHICKEN, null);
        food2 = foodTestHelper.createFood("food2", company, Category.PIZZA, null);
    }

    @DisplayName("1. 회사를 생성한다.")
    @Test
    void test_1() {
        // Company company = companyTestHelper.createCompany("test company", "companyLogo");
        Company savedCompany = companyService.findByCompanyId(company.getCompanyId());
        companyTestHelper.assertCompany(savedCompany, "test company", "companyLogo");
    }

    @DisplayName("2. 회사의 음식/메뉴를 추가한다.")
    @Test
    void test_2() {
        companyService.companyAddFood(company.getCompanyId(), food1);
        assertEquals(1, company.getFoods().size());
        companyService.companyAddFood(company.getCompanyId(), food2);
        assertEquals(2, company.getFoods().size());

        assertEquals("food1", company.getFoods().get(0).getName());
        assertEquals("food2", company.getFoods().get(1).getName());
    }

    @DisplayName("3. 회사의 음식/메뉴는 중복하여 들어가지 않는다.")
    @Test
    void test_3() {
        companyService.companyAddFood(company.getCompanyId(), food1);
        companyService.companyAddFood(company.getCompanyId(), food1);
        assertEquals(1, company.getFoods().size());
        assertEquals("food1", company.getFoods().iterator().next().getName());
    }

    @DisplayName("4. 회사의 이름, 이미지URL를 수정한다.")
    @Test
    void test_4() {
        companyService.updateCompanyName(company.getCompanyId(), "ChickenPlus");
        companyService.updateCompanyImage(company.getCompanyId(), "NewCompanyLogo");
        Company savedCompany = companyService.findByCompanyId(company.getCompanyId());
        assertEquals("ChickenPlus", savedCompany.getName());
        assertEquals("NewCompanyLogo", savedCompany.getImage());
    }

    @DisplayName("5. 회사의 음식을 삭제한다.")
    @Test
    void test_5() {
        companyService.companyAddFood(company.getCompanyId(), food1);
        companyService.companyAddFood(company.getCompanyId(), food2);

        companyService.CompanyRemoveFood(company.getCompanyId(), food1);
        foodService.FoodRemoveCompany(food1.getFoodId());

        Company savedCompany = companyService.findByCompanyId(company.getCompanyId());
        assertEquals(1, savedCompany.getFoods().size());
        assertEquals("food2", savedCompany.getFoods().iterator().next().getName());

        System.out.println(">>> "+savedCompany.getFoods());
    }



}

/**
 * 회사를 생성한다
 * 회사의 음식/메뉴들을 추가한다.
 * 회사의 음식/메뉴들, 이미지URL를 수정한다.
 * 회사의 음식을 삭제한다.
 * 회사의 메뉴들을 조회한다.
 */