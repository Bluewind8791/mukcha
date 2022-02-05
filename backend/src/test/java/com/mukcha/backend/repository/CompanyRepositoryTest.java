package com.mukcha.backend.repository;

import com.mukcha.backend.domain.Category;
import com.mukcha.backend.domain.Company;
import com.mukcha.backend.repository.helper.RepositoryTestHelper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class CompanyRepositoryTest {

    @Autowired private CompanyRepository companyRepository;
    @Autowired private FoodRepository foodRepository;
    private RepositoryTestHelper repositoryTestHelper;

    @BeforeEach
    void before() {
        this.repositoryTestHelper = new RepositoryTestHelper(foodRepository, companyRepository);
        Company company = repositoryTestHelper.createCompany("치킨플러스", "imageUrl");
        repositoryTestHelper.createFood("원헌드RED", company, Category.CHICKEN, "testImage");
    }

    @DisplayName("1. findById Test")
    @Test
    void test_1() {
        System.out.println(companyRepository.findById(1L));
        System.out.println(foodRepository.findById(1L));
    }

    @DisplayName("2. findByName Test")
    @Test
    void test_2() {
        System.out.println(companyRepository.findByName("치킨플러스"));
    }



}
