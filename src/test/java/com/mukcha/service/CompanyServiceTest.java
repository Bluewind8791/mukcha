package com.mukcha.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.service.helper.WithTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@SpringBootTest
@ActiveProfiles("local")
public class CompanyServiceTest extends WithTest {

    @BeforeEach
    protected void before() {
        prepareTest();
    }


    @Test
    @DisplayName("1. 회사를 생성한다.")
    void test_1() {
        Company company = companyTestHelper.createCompany("test company", "companyLogo");
        Company savedCompany = companyService.findCompany(company.getCompanyId());
        companyTestHelper.assertCompany(savedCompany, "test company", "companyLogo");
    }

    @Test
    @DisplayName("2. 회사의 음식/메뉴를 추가한다.")
    void test_2() {
        Company company = companyTestHelper.createCompany("test company", "companyLogo");
        Food food1 = foodTestHelper.createFood("food1", company, Category.CHICKEN, null);
        Food food2 = foodTestHelper.createFood("food2", company, Category.PIZZA, null);

        companyService.companyAddFood(company.getCompanyId(), food1);
        assertEquals(1, company.getFoods().size());
        companyService.companyAddFood(company.getCompanyId(), food2);
        assertEquals(2, company.getFoods().size());

        assertEquals("food1", company.getFoods().get(0).getName());
        assertEquals("food2", company.getFoods().get(1).getName());
    }

    @Test
    @DisplayName("3. 회사의 음식/메뉴는 중복하여 들어가지 않는다.")
    void test_3() {
        // set
        Company company = companyTestHelper.createCompany("test company", "companyLogo");
        Food food1 = foodTestHelper.createFood("food1", company, Category.CHICKEN, null);
        // add
        companyService.companyAddFood(company.getCompanyId(), food1);
        companyService.companyAddFood(company.getCompanyId(), food1);
        // assert
        assertEquals(1, company.getFoods().size());
        assertEquals("food1", company.getFoods().iterator().next().getName());
    }

    @Test
    @DisplayName("4. 회사의 이름, 이미지URL를 수정한다.")
    void test_4() {
        // set
        Company company = companyTestHelper.createCompany("test company", "companyLogo");
        // edit
        companyService.editCompanyName(company.getCompanyId(), "ChickenPlus");
        companyService.editCompanyLogo(company.getCompanyId(), "NewCompanyLogo");
        // assert
        Company savedCompany = companyService.findCompany(company.getCompanyId());
        assertEquals("ChickenPlus", savedCompany.getName());
        assertEquals("NewCompanyLogo", savedCompany.getImage());
    }

    @Test
    @DisplayName("5. 회사를 삭제한다.")
    void test_5() {
        // set
        Company company = companyTestHelper.createCompany("test company", "companyLogo");
        Food food1 = foodTestHelper.createFood("food1", company, Category.CHICKEN, null);
        Long companyId = company.getCompanyId();
        System.out.println(">>> "+company.getFoods());
        // delete
        companyService.deleteCompany(companyId);
        // assert
        assertThrows(IllegalArgumentException.class, () -> companyService.findCompany(companyId));
        assertNull(food1.getCompany());
    }

    @Test
    @DisplayName("6. findCompanyTopTenNewest")
    void test_6() {
        // set
        Company company = companyTestHelper.createCompany("test company", "companyLogo");
        foodTestHelper.createFood("food1", company, Category.CHICKEN, null);
        foodTestHelper.createFood("food2", company, Category.PIZZA, null);
        // get
        List<Company> companies = companyService.findCompanyTopTenNewest();
        // assert
        // assertEquals(10, companies.size());
        System.out.println(">>> "+companies);
    }


    @Test
    @DisplayName("회사의 해당 메뉴를 삭제한다.")
    void testDeleteFood() {
        // given
        Company company = companyTestHelper.createCompany("testCompany", "companyLogo");
        Long companyId = company.getCompanyId();
        Food food1 = foodTestHelper.createFood("food1", company, Category.CHICKEN, null);
        foodTestHelper.createFood("food2", company, Category.PIZZA, null);
        System.out.println(">>> company.getFoods: "+companyService.getFoodList(companyId).size());
        // when
        companyService.deleteFood(companyId, food1.getFoodId());
        // then
        Company savedCompany = companyService.findCompany(companyId);
        List<Food> foods = companyService.getFoodList(savedCompany.getCompanyId());
        System.out.println(">>> company.getFoods: "+foods);
        assertEquals(1, foods.size());
        assertEquals("food2", foods.get(0).getName());
    }



}