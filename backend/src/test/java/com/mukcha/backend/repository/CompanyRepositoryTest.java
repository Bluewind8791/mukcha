package com.mukcha.backend.repository;

import com.mukcha.backend.domain.Category;
import com.mukcha.backend.domain.Company;
import com.mukcha.backend.domain.Food;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private FoodRepository foodRepository;

    @DisplayName("1. Company Repository Test")
    @Test
    void test_1() {

        givenFood();
        
        System.out.println(companyRepository.findById(1L));
        System.out.println(foodRepository.findById(1L));
    }

    private Food givenFood() {
        Food food = new Food();
        food.setName("원헌드RED");
        food.setCategory(Category.CHICKEN);
        food.setCompany(givenCompany());

        return foodRepository.save(food);
    }

    private Company givenCompany() {
        Company company = new Company();
        company.setName("치킨플러스");
        return companyRepository.save(company);
    }


}
