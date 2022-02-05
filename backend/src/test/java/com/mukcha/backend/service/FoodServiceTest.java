package com.mukcha.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

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
public class FoodServiceTest {

    @Autowired private FoodRepository foodRepository;
    @Autowired private CompanyService companyService;
    @Autowired private CompanyRepository companyRepository;

    private FoodService foodService;
    private FoodTestHelper foodTestHelper;
    private CompanyTestHelper companyTestHelper;
    Food food;
    Company company;
    
    @BeforeEach
    void before() {
        this.foodRepository.deleteAll();
        this.companyRepository.deleteAll();
        this.foodService = new FoodService(foodRepository);
        this.foodTestHelper = new FoodTestHelper(this.foodService);
        this.companyTestHelper = new CompanyTestHelper(this.companyService);

        company = this.companyTestHelper.createCompany("test company", "imageUrl");
        food = this.foodTestHelper.createFood("test food", company, Category.CHICKEN, "imageUrl");
    }

    @DisplayName("1. 음식을 생성한다")
    @Test
    void test_1() {
        List<Food> list = foodRepository.findAll();
        assertEquals(1, list.size());
        foodTestHelper.assertFood(
            list.get(0), 
            list.get(0).getName(), 
            list.get(0).getCompany(), 
            list.get(0).getCategory(), 
            list.get(0).getImage()
        );
    }

    @DisplayName("2. 음식 이름, 회사, 이미지, 카테고리를 수정한다.")
    @Test
    void test_2() {
        foodService.editFoodName(food.getId(), "test2 food");
        foodService.editFoodImage(food.getId(), "imageUrl2");
        foodService.editFoodCategory(food.getId(), Category.PIZZA);
        
        assertEquals("test2 food", foodRepository.findAll().get(0).getName());
        assertEquals("imageUrl2", foodRepository.findAll().get(0).getImage());
        assertEquals(Category.PIZZA, foodRepository.findAll().get(0).getCategory());

        // Company company2 = this.companyTestHelper.createCompany("test2 company", "imageUrl2");
        // foodService.editFoodCompany(food.getId(), "test2 company");
        // assertEquals("test2 company", foodRepository.findAll().get(0).getCompany().getName());
    }

    @DisplayName("3. 카테고리를 가져온다")
    @Test
    void test_3() {
        List<String> list = foodService.categories();
        assertEquals(1, list.size());
        assertEquals("CHICKEN", list.get(0));

        foodTestHelper.createFood("test2 food", null, Category.PIZZA, null);
        list = foodService.categories();
        assertEquals(2, list.size());
    }

    @DisplayName("4. 카테고리 별 음식을 가져온다")
    @Test
    void test_4() {
        List<Food> list = foodService.findAllByCategory(Category.CHICKEN);
        assertEquals(1, list.size());

        foodTestHelper.createFood("test2 food", null, Category.CHICKEN, null);
        list = foodService.findAllByCategory(Category.CHICKEN);
        assertEquals(2, list.size());
    }



}
