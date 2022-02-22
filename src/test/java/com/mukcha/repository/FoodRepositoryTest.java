package com.mukcha.repository;

import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
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

    @BeforeEach
    void before() {
        this.repositoryTestHelper = new RepositoryTestHelper(foodRepository, companyRepository);
        Company company = repositoryTestHelper.createCompany("치킨플러스", "imageUrl");
        repositoryTestHelper.createFood("원헌드RED", company, Category.CHICKEN, "testImage");
    }

    @DisplayName("1. findAll Test")
    @Test
    void test_1() {
        System.out.println(foodRepository.findAll());
    }

    @DisplayName("2. getCategories Test")
    @Test
    void test_2() {
        System.out.println(foodRepository.getAllCategories());
    }




}
