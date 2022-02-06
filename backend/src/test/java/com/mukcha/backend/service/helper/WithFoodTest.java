package com.mukcha.backend.service.helper;

import com.mukcha.backend.domain.Category;
import com.mukcha.backend.domain.Company;
import com.mukcha.backend.domain.Food;
import com.mukcha.backend.repository.CompanyRepository;
import com.mukcha.backend.repository.FoodRepository;
import com.mukcha.backend.service.CompanyService;
import com.mukcha.backend.service.FoodService;

import org.springframework.beans.factory.annotation.Autowired;

public class WithFoodTest {
    
    @Autowired protected FoodRepository foodRepository;
    @Autowired protected CompanyRepository companyRepository;

    @Autowired protected CompanyService companyService;
    @Autowired protected FoodService foodService;

    protected FoodTestHelper foodTestHelper;
    protected CompanyTestHelper companyTestHelper;
    protected Food food;
    protected Company company;

    protected void prepareFoodTest() {
        this.foodRepository.deleteAll();
        this.companyRepository.deleteAll();

        this.foodService = new FoodService(foodRepository);
        this.foodTestHelper = new FoodTestHelper(this.foodService);
        this.companyTestHelper = new CompanyTestHelper(this.companyService);

        this.company = this.companyTestHelper.createCompany("test company", "companyLogo");
        this.food = this.foodTestHelper.createFood("test food", company, Category.CHICKEN, "menuImage");
    }
}
