package com.mukcha.backend.domain;

import com.mukcha.backend.repository.CompanyRepository;
import com.mukcha.backend.repository.FoodRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class CompanyTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private FoodRepository foodRepository;


    @Test
    void testAddFood() {
        Food food = new Food();
        food.setName("testFood");
        // foodRepository.save(food);

        Company company = new Company();
        company.setName("testCompany");
        company.setImage("logoImage");
        company.addFood(food);
        companyRepository.save(company);

        System.out.println(">>> " + companyRepository.findAll());
        System.out.println(">>> " + foodRepository.findAll());

        // company.setName("changed");
        // companyRepository.save(company);
        // System.out.println(">>> " + company);
        // System.out.println(">>> " + food);
    }
}
