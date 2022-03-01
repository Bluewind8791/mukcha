package com.mukcha.service;

import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.repository.CompanyRepository;
import com.mukcha.repository.FoodRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class FoodService {
    
    private final FoodRepository foodRepository;

    @Autowired
    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @Autowired private CompanyRepository companyRepository;
    
    
    // 음식을 생성한다.
    public Food save(Food food) {
        if (food.getFoodId() == null) {
            food.setCreatedAt(LocalDateTime.now());
        }
        food.setUpdatedAt(LocalDateTime.now());

        return foodRepository.save(food);
    }

    // 음식 이름을 수정한다
    public Optional<Food> editFoodName(Long foodId, String name) {
        return foodRepository.findById(foodId).map(food -> {
            food.setName(name);
            foodRepository.save(food);
            return food;
        });
    }
    
    // 음식 이미지 url 을 수정한다
    public Optional<Food> editFoodImage(Long foodId, String imageUrl) {
        return foodRepository.findById(foodId).map(food -> {
            food.setImage(imageUrl);
            foodRepository.save(food);
            return food;
        });
    }

    // 음식 카테고리를 수정한다
    public Optional<Food> editFoodCategory(Long foodId, Category category) {
        return foodRepository.findById(foodId).map(food -> {
            food.setCategory(category);
            foodRepository.save(food);
            return food;
        });
    }

    // 음식 회사를 수정한다
    public Optional<Food> editFoodCompany(Long foodId, String companyName) {
        try {
            companyRepository.findByName(companyName); 
        } catch (NullPointerException e) { // 존재하지 않는 회사라면
            Company saveCompany = Company.builder().name(companyName).build();
            companyRepository.save(saveCompany);
            return foodRepository.findById(foodId).map(food -> {
                food.setCompany(saveCompany);
                foodRepository.save(food);
                return food;
            });
        }
        // 존재하는 회사라면
        Company cmp = companyRepository.findByName(companyName);
        return foodRepository.findById(foodId).map(food -> {
            food.setCompany(cmp);
            foodRepository.save(food);
            return food;
        });
    }


    // food 에서 company 와의 연관관계를 끊는다. (null 처리)
    public void FoodRemoveCompany(Long foodId) {
        Food targetFood = foodRepository.findById(foodId).orElseThrow(() -> 
        new IllegalArgumentException("해당 음식/메뉴를 찾을 수 없습니다.")
        );
        targetFood.setCompany(null);
        foodRepository.save(targetFood);
    }



    public List<String> categories() {
        return foodRepository.getAllCategories();
    }

    public List<Food> findAllByCategory(Category category) {
        return foodRepository.findAllByCategory(category);
    }

    public Optional<Food> findFood(Long foodId) {
        return foodRepository.findById(foodId);
    }






// - 회사 목록을 가져온다
// - 회사별 음식 목록을 가져온다
// - 카테고리를 가져온다
// - 카테고리 별 음식을 가져온다

}