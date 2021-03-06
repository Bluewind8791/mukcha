package com.mukcha.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.mukcha.controller.dto.FoodSaveRequestDto;
import com.mukcha.controller.dto.FoodUpdateRequestDto;
import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.domain.User;
import com.mukcha.service.helper.WithTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@SpringBootTest
@ActiveProfiles("test2")
public class FoodServiceTest extends WithTest {

    User user;
    Company company;

    @BeforeEach
    void before() {
        prepareTest();
        this.user = this.userTestHelper.createUser("testuser@test.com", "testuser");
        this.company = this.companyTestHelper.createCompany("testCompany", "companyLogo");
    }


    @Test
    @DisplayName("메뉴를 새로 생성한다. save")
    void test_1() {
        // given
        String menuName = "foodName";
        // when
        boolean result = foodService.save(
            new FoodSaveRequestDto(menuName, "foodImage", "CHICKEN", company.getName())
        );
        // then
        assertTrue(result);
        Food food = foodService.findByNameAndCompany(company, menuName);
        assertEquals("foodName", food.getName());
        assertEquals("foodImage", food.getImage());
        assertEquals(Category.CHICKEN, food.getCategory());
        assertEquals(company, food.getCompany());
    }

    @Test
    @DisplayName("메뉴 정보를 수정한다 update")
    void test_2() {
        // given
        Food food = foodTestHelper.createFood("name", company, Category.CHICKEN, "image");
        // when
        boolean result = foodService.update(food.getFoodId(), new FoodUpdateRequestDto("foodName", "foodImage", "PIZZA"));
        // then
        Food savedFood = foodService.findByFoodId(food.getFoodId());
        assertEquals(result, true);
        assertEquals("foodName", savedFood.getName());
        assertEquals("foodImage", savedFood.getImage());
        assertEquals(Category.PIZZA, savedFood.getCategory());
    }

    @Test
    @DisplayName("카테고리 별 음식을 가져온다 findAllByCategory")
    void test_4() {
        // given
        foodTestHelper.createFood("name", company, Category.CHICKEN, "image");
        // when
        List<Food> list = foodService.findAllByCategory(Category.CHICKEN);
        // then
        assertTrue(
            list.stream().map(food -> food.getCategory()).allMatch(c -> c == Category.CHICKEN)
        );
    }

    @Test
    @DisplayName("해당 회사의 메뉴들 중 해당 메뉴가 있는지 확인")
    void testIsPresentByCompanyAndFoodName() {
        // given
        Company company = companyTestHelper.createCompany("testCompany", "companyLogo");
        Food food = foodTestHelper.createFood("food", company, Category.CHICKEN, null);
        // when
        boolean result1 = foodService.isPresentByCompanyAndFoodName(company, food.getName());
        // then
        assertEquals(true, result1);
    }


}