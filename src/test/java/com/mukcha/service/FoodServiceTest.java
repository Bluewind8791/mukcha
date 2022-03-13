package com.mukcha.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.mukcha.controller.dto.FoodDto;
import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.domain.Score;
import com.mukcha.domain.User;
import com.mukcha.service.helper.WithTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@SpringBootTest // 22.3.11
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
    @DisplayName("1. 음식을 생성한다")
    void test_1() {
        // create
        foodTestHelper.createFood("FoodTest1", company, Category.HAMBURGER, "image");
        // assert
        Food savedFood = foodService.findByName("FoodTest1").get();
        assertEquals("FoodTest1", savedFood.getName());
        assertEquals(company, savedFood.getCompany());
        assertEquals(Category.HAMBURGER, savedFood.getCategory());
        assertEquals("image", savedFood.getImage());
    }

    @Test
    @DisplayName("2. 음식 이름, 회사, 이미지, 카테고리를 수정한다.")
    void test_2() {
        // set
        Food food = foodTestHelper.createFood("testFood", company, Category.CHICKEN, "menuImage");
        // edit
        foodService.editFoodName(food.getFoodId(), "test2 food");
        foodService.editFoodImage(food.getFoodId(), "imageUrl2");
        foodService.editFoodCategory(food.getFoodId(), Category.PIZZA);
        foodService.editFoodCompany(food.getFoodId(), "testCompany2");
        // assert
        Food savedFood = foodService.findFood(food.getFoodId()).get();
        assertEquals("test2 food", savedFood.getName());
        assertEquals("imageUrl2", savedFood.getImage());
        assertEquals(Category.PIZZA, savedFood.getCategory());
        assertEquals("testCompany2", savedFood.getCompany().getName());
    }

    @Test
    @DisplayName("4. 카테고리 별 음식을 가져온다")
    void test_4() {
        // DB init 5 + test helper 1 = 6
        List<Food> list = foodService.findAllByCategory(Category.CHICKEN);

        assertTrue(
            list.stream().map(food -> food.getCategory()).allMatch(c -> c == Category.CHICKEN)
        );
    }

    @Test
    @DisplayName("5. 음식의 평균 별점을 가져온다")
    void test_5() {
        // set
        Food food = foodTestHelper.createFood("testFood", company, Category.CHICKEN, "menuImage");
        reviewTestHelper.createReviewWithScore(food, user, Score.BAD);  // 1
        reviewTestHelper.createReviewWithScore(food, user, Score.GOOD); // 4
        Food savedFood = foodService.findByName(food.getName()).get();
        // get
        double averageScore = foodService.getAverageScoreByFoodId(savedFood.getFoodId());
        // assert - (1+4)/2
        assertEquals(2.5F, averageScore);
    }

    @Test
    @DisplayName("6. 평균별점을 기준으로 상위 10개의 메뉴를 가져온다")
    void test_6() {
        // set
        Food food1 = foodTestHelper.createFood("ttest1", company, Category.HAMBURGER, null);
        Food food2 = foodTestHelper.createFood("ttest2", company, Category.HAMBURGER, null);
        Food food3 = foodTestHelper.createFood("ttest3", company, Category.HAMBURGER, null);
        Food food4 = foodTestHelper.createFood("ttest4", company, Category.HAMBURGER, null);
        Food food5 = foodTestHelper.createFood("ttest5", company, Category.HAMBURGER, null);
        reviewTestHelper.createReviewWithScore(food1, user, Score.GOOD);
        reviewTestHelper.createReviewWithScore(food2, user, Score.GOOD);
        reviewTestHelper.createReviewWithScore(food3, user, Score.GOOD);
        reviewTestHelper.createReviewWithScore(food4, user, Score.GOOD);
        reviewTestHelper.createReviewWithScore(food5, user, Score.GOOD);
        // get
        List<FoodDto> foodDtos = foodService.findTopTenOrderByScore();
        // assert
        assertEquals(10, foodDtos.size());
    }

    @Test
    @DisplayName("7. 가장 최신의 메뉴 10개를 가져온다.")
    void test_7() {
        // get
        List<FoodDto> allFoods = foodService.findTopTenNewest();
        // assert
        assertEquals(10, allFoods.size());
    }

    @Test
    @DisplayName("8. 음식을 삭제한다.")
    void test_8() {
        // set
        Food food = foodTestHelper.createFood("ttest1", company, Category.HAMBURGER, null);
        reviewTestHelper.createReview(food, user);
        Long foodId = food.getFoodId();

        System.out.println(">>> food: "+food.toString());
        System.out.println(">>> review: "+reviewRepository.findAllByFoodId(food.getFoodId()));
        System.out.println(">>> 회사의 음식정보: "+companyService.getFoodListInfo(company.getCompanyId()));

        // delete
        foodService.deleteFood(food.getFoodId());
        System.out.println(">>> 삭제 <<<");
        
        // 삭제 확인
        System.out.println(">>> review: "+reviewRepository.findAllByFoodId(foodId));
        System.out.println(">>> company: "+companyService.findCompany(company.getCompanyId()));
        System.out.println(">>> 회사의 음식정보: "+companyService.getFoodListInfo(company.getCompanyId()));
        
        // assert
        assertThrows(NoSuchElementException.class, () -> foodService.findByName("ttest1").get());
        // 해당 회사에 삭제한 음식이 있는지 검사
        assertEquals(List.of(), companyService.getFoodListInfo(company.getCompanyId()));
    }

    @Test
    @DisplayName("9. 음식 리스트를 회사 별로 분류한다.")
    void test_9() {
        List<Company> companyList = companyService.findAll();
        Category thisCategory = Category.valueOf("CHICKEN");
        List<Food> foodList = foodService.findAllByCategory(thisCategory);
        Map<String, List<Food>> foodMap = new HashMap<>();
        
        for (Company com : companyList) {
            List<Food> foodPerCompany = new ArrayList<>();
            // getCompany가 null로 뜨는듯
            // 
            foodPerCompany.addAll(foodList.stream().filter(f -> 
                f.getCompany() == com).collect(Collectors.toList())
            );
            foodMap.put(com.getName(), foodPerCompany);
        }
        System.out.println(">>> foodMap: "+foodMap);
    }



}