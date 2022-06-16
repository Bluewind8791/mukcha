package com.mukcha.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import com.mukcha.domain.Category;
import com.mukcha.domain.Food;
import com.mukcha.repository.helper.WithRepositoryTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@SpringBootTest
@ActiveProfiles("local")
public class FoodRepositoryTest extends WithRepositoryTest {

    @BeforeEach
    void before() {
        prepareTest();
    }


    @Test
    void testFindByName() {
        // when
        Food foundFood = foodRepository.findByName("testFood").get();
        // then
        assertEquals("testFood", foundFood.getName());
        assertEquals("testFoodImage", foundFood.getImage());
    }

    @Test
    void testFindAllByCategory() {
        List<Food> foundFoods = foodRepository.findAllByCategory(Category.CHICKEN);
        assertNotNull(foundFoods);
    }

    @Test
    void testFindAllByCompany() {
        List<Food> foundFoods = foodRepository.findAllByCompany(company);
        Food foundFood = foundFoods.get(0);
        assertEquals("testFood", foundFood.getName());
    }

    @Test
    void testFindAllCategories() {
        List<String> categories = foodRepository.findAllCategories();
        assertNotNull(categories);
    }

    @Test
    void testDeleteById() {
        foodRepository.deleteById(food.getFoodId());
        assertEquals(Optional.empty(), foodRepository.findById(food.getFoodId()));
    }

    @Test
    void testFindAllByCompanyAndCategory() {
        List<Food> foundFoods = foodRepository.findAllByCompanyAndCategory(company, Category.CHICKEN);
        Food foundFood = foundFoods.get(0);
        assertEquals("testFood", foundFood.getName());
        assertEquals("testFoodImage", foundFood.getImage());
    }

    @Test
    void testFindUpdatedAtTopTen() {
        List<Food> foundFoods = foodRepository.findTop10ByOrderByUpdatedAtDesc();
        for (Food food : foundFoods) {
            System.out.println(food);
        }
    }

}