package com.mukcha.backend.service;


import java.util.List;

import com.mukcha.backend.domain.Company;
import com.mukcha.backend.domain.Food;
import com.mukcha.backend.repository.CompanyRepository;
import com.mukcha.backend.repository.FoodRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Autowired
    private FoodRepository foodRepository;

    public Company save(Company company) {
        return companyRepository.save(company);
    }

    public Company findByCompanyId(Long companyId) {
        return companyRepository.findById(companyId).orElseThrow(() -> new IllegalArgumentException("해당 회사를 찾을 수 없습니다."));
    }

    public void companyAddFood(Long companyId ,Food food) {
        // find company
        companyRepository.findById(companyId).ifPresentOrElse(company -> {
            // company foods 에 food 가 없다면
            if (!company.getFoods().contains(food)) {
                company.addFood(food);
                save(company);
            }
        }, () -> new IllegalArgumentException("해당 회사를 찾을 수 없습니다."));
    }

    public void updateCompanyImage(Long companyId, String image) {
        companyRepository.updateCompanyImage(companyId, image);
    }

    public void updateCompanyName(Long companyId, String name) {
        companyRepository.updateCompanyName(companyId, name);
    }

    public void CompanyRemoveFood(Long companyId, Food food) {
        // 해당 company id 로 찾기
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new IllegalArgumentException("해당 회사를 찾을 수 없습니다."));

        List<Food> originFoods = company.getFoods();
        // 기존 food 리스트에 삭제하려는 food가 들어있다면
        if (originFoods.contains(food)) {
            originFoods.remove(food);
            // companyAddFood(companyId, food);
            companyRepository.save(company);
        }
        // food 에서 company 항목 null 처리
        // Food targetFood = foodRepository.findById(food.getFoodId()).orElseThrow(() -> 
        //     new IllegalArgumentException("해당 음식/메뉴를 찾을 수 없습니다.")
        //     );
        // targetFood.setCompany(null);
        // foodRepository.save(targetFood);
    }


}
