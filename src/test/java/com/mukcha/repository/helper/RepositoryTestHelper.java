package com.mukcha.repository.helper;

import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.repository.CompanyRepository;
import com.mukcha.repository.FoodRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RepositoryTestHelper {
    
    private final FoodRepository foodRepository;
    private final CompanyRepository companyRepository;

    public Food makeFood(String name, Company company, Category category, String image) {
        return Food.builder()
                .name(name)
                .company(company)
                .category(category)
                .image(image)
                .build();
    }

    public Food createFood(String name, Company company, Category category, String image) {
        return foodRepository.save(makeFood(name, company, category, image));
    }

    public Company makeCompany(String name, String imageUrl) {
        return Company.builder()
                .name(name)
                .image(imageUrl)
                .build();
    }

    public Company createCompany(String name, String imageUrl) {
        return companyRepository.save(makeCompany(name, imageUrl));
    }
}
