package com.mukcha.backend.repository;

import com.mukcha.backend.domain.Category;
import com.mukcha.backend.domain.Company;
import com.mukcha.backend.domain.Food;
import com.mukcha.backend.repository.helper.RepositoryTestHelper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class CompanyRepositoryTest {

    @Autowired private CompanyRepository companyRepository;
    @Autowired private FoodRepository foodRepository;

    private RepositoryTestHelper repositoryTestHelper;
    Company company;
    Food food;

    @BeforeEach
    void before() {
        this.repositoryTestHelper = new RepositoryTestHelper(foodRepository, companyRepository);
        this.company = this.repositoryTestHelper.createCompany("치킨플러스", "imageUrl");
        this.food = this.repositoryTestHelper.createFood("원헌드RED", company, Category.CHICKEN, "testImage");
    }

    @Test
    void testFindById() {
        System.out.println(companyRepository.findById(1L));
        System.out.println(foodRepository.findById(1L));
    }

    @Test
    void testFindByFoodId() {
        // System.out.println(companyRepository.findByFoodsId(company.getCompanyId(), food.getFoodId()));
    }



}
