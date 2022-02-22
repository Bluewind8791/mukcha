package com.mukcha.service.helper;

import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.repository.CompanyRepository;
import com.mukcha.repository.FoodRepository;
import com.mukcha.service.CompanyService;
import com.mukcha.service.FoodService;

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
