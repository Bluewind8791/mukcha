package com.mukcha.backend.service.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mukcha.backend.domain.Category;
import com.mukcha.backend.domain.Company;
import com.mukcha.backend.domain.Food;
import com.mukcha.backend.service.FoodService;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class FoodTestHelper {

    private final FoodService foodService;

    public Food makeFood(String name, Company company, Category category, String image) {
        return Food.builder()
                .name(name)
                .company(company)
                .category(category)
                .image(image)
                .build();
    }

    public Food createFood(String name, Company company, Category category, String image) {
        return foodService.save(makeFood(name, company, category, image));
    }

    public void assertFood(Food food, String name, Company company, Category category, String image) {
        assertNotNull(food.getName());
        assertNotNull(food.getCompany());
        assertNotNull(food.getCategory());
        assertNotNull(food.getCreatedAt());
        assertNotNull(food.getUpdatedAt());
        assertNotNull(food.getImage());

        assertEquals(name, food.getName());
        assertEquals(company, food.getCompany());
        assertEquals(category, food.getCategory());
        assertEquals(image, food.getImage());
    }
    
}
