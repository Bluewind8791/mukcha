package com.mukcha.repository;

import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.repository.helper.RepositoryTestHelper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class FoodRepositoryTest {

    @Autowired private FoodRepository foodRepository;
    @Autowired private CompanyRepository companyRepository;

    private RepositoryTestHelper repositoryTestHelper;

    private Company company;
    private Food food;

    @BeforeEach
    void before() {
        this.repositoryTestHelper = new RepositoryTestHelper(foodRepository, companyRepository);
        this.company = repositoryTestHelper.createCompany("TestCompany1", "imageUrl");
        this.food = repositoryTestHelper.createFood("TestFood1", company, Category.CHICKEN, "testImage");
    }

    @Test
    @DisplayName("1. findAll Test") // 22.3.5
    void test_1() {
        System.out.println(">>> findAll: "+foodRepository.findAll());
    }

    @Test
    @DisplayName("2. getCategories Test") // 22.3.5
    void test_2() {
        System.out.println(">>> getAllCategories: "+foodRepository.getAllCategories());
    }

    @Test
    @DisplayName("3. findByName Test") // 22.3.5
    void test_3() {
        System.out.println(">>> findByName: "+foodRepository.findByName("TestFood1"));
    }




}
