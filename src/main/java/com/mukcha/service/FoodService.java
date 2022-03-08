package com.mukcha.service;

import com.mukcha.controller.dto.FoodDto;
import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.domain.Review;
import com.mukcha.repository.CompanyRepository;
import com.mukcha.repository.FoodRepository;
import com.mukcha.repository.ReviewRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;


@Service
@Transactional
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final CompanyRepository companyRepository;
    private final ReviewRepository reviewRepository;



    // 음식을 생성한다.
    public Food save(Food food) {
        return foodRepository.save(food);
    }


    /* EDIT METHODS */
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
        if (companyRepository.findByName(companyName).isPresent()) {
            // 존재하는 회사라면
            Company cmp = companyRepository.findByName(companyName).get();
            return foodRepository.findById(foodId).map(food -> {
                food.setCompany(cmp);
                foodRepository.save(food);
                return food;
            });
        } else {
            // 존재하지 않는 회사라면 새로 만들기
            Company saveCompany = Company.builder().name(companyName).build();
            companyRepository.save(saveCompany);
            return foodRepository.findById(foodId).map(food -> {
                food.setCompany(saveCompany);
                foodRepository.save(food);
                return food;
            });
        }
    }


    // food 에서 company 와의 연관관계를 끊는다. (null 처리)
    public void FoodRemoveCompany(Long foodId) {
        Food targetFood = foodRepository.findById(foodId).orElseThrow(() -> 
            new IllegalArgumentException("해당 음식/메뉴를 찾을 수 없습니다.")
        );
        targetFood.setCompany(null);
        foodRepository.save(targetFood);
    }





    /* FIND methods */
    public Optional<Food> findFood(Long foodId) {
        return foodRepository.findById(foodId);
    }
    public List<Food> findAll() {
        return foodRepository.findAll();
    }
    public Optional<Food> findByName(String foodName) {
        return foodRepository.findByName(foodName);
    }
    public List<String> categories() {
        return foodRepository.getAllCategories();
    }
    public List<Food> findAllByCategory(Category category) {
        return foodRepository.findAllByCategory(category);
    }


    /* VIEW methods */
    @Transactional(readOnly = true)
    public FoodDto viewFoodDetail(Long foodId) {
        Food food = foodRepository.findById(foodId).orElseThrow(() -> {
            return new IllegalArgumentException("해당 메뉴를 찾을 수 없습니다.");
        });
        FoodDto foodDto = new FoodDto();
        foodDto.setFoodId(food.getFoodId());
        foodDto.setFoodName(food.getName());
        foodDto.setCompany(food.getCompany().getName());
        foodDto.setCategory(food.getCategory().toString());
        foodDto.setFoodImage(food.getImage());
        foodDto.setAverageScore(getAverageScoreByFoodId(food.getFoodId()));
        return foodDto;
    }

    // foodDto로 return 받는 find all food
    @Transactional(readOnly = true)
    public List<FoodDto> findAllWithAverageScore() {
        // food list를 가져와서
        List<Food> foods = foodRepository.findAll();
        List<FoodDto> foodDtos = new ArrayList<>();
        // FoodDto 로 변환
        foods.stream().forEach(food -> {
            FoodDto foodDto = new FoodDto();
            foodDto.setFoodId(food.getFoodId());
            foodDto.setFoodName(food.getName());
            foodDto.setCompany(food.getCompany().getName());
            foodDto.setCategory(food.getCategory().toString());
            foodDto.setFoodImage(food.getImage());
            foodDto.setAverageScore(getAverageScoreByFoodId(food.getFoodId()));
            foodDtos.add(foodDto);
        });
        return foodDtos;
    }


    public double getAverageScoreByFoodId(Long foodId) {        

        // List<Review> reviewList = reviewService.findAllReviewByFoodId(foodId);
        List<Review> reviewList = reviewRepository.findAllByFoodId(foodId);
        List<Integer> scoreList = reviewList.stream().map(
            r -> r.getScore().value).collect(Collectors.toList()
        );
        if (scoreList.isEmpty()) {
            return 0;
        }
        int total = scoreList.stream().mapToInt(Integer::intValue).sum();
        float avgScore = (total / (float)scoreList.size());
        return Math.round(avgScore*100)/100.0;
    }






    // public Page<Food> listOrderByScore(int pageNum, int size) {
        // return foodReviewInfoRepository.(PageRequest.of(pageNum, size));
    // }


}