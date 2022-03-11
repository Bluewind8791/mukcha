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
import java.util.Collections;
import java.util.Comparator;
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


    // 음식을 삭제한다
    @Transactional
    public void deleteFood(Long foodId) {
        Food targetFood = findFood(foodId).orElseThrow(
            () -> new IllegalArgumentException("해당 음식을 찾을 수 없습니다.")
        );
        // 연결된 리뷰 모두 삭제 
        reviewRepository.deleteAllByFoodId(foodId);
        // 회사 null 처리 ?
        targetFood.setCompany(null);
        foodRepository.delete(targetFood);
        if (findFood(foodId).isPresent()) {
            throw new IllegalArgumentException("삭제에 실패하였습니다.");
        }
    }



    /* FIND methods */
    public Optional<Food> findFood(Long foodId) {
        return foodRepository.findById(foodId);
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
    // Find All
    @Transactional(readOnly = true)
    public List<Food> findAll() {
        return foodRepository.findAll();
    }

    // FoodDto로 변환하여 메뉴 정보 return
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

    // foodDto 변환하여 모든 메뉴의 평균점수와 함께 리턴
    @Transactional(readOnly = true)
    public List<FoodDto> findAllWithAverageScore() {
        // food list를 가져와서
        List<Food> foods = foodRepository.findAll();
        List<FoodDto> foodDtos = convertFoodToDto(foods);
        return foodDtos;
    }

    // 평균 점수를 기준으로 모든 10개의 메뉴를 가져오기
    @Transactional(readOnly = true)
    public List<FoodDto> findTopTenOrderByScore() {
        // get
        List<FoodDto> foodDtos = findAllWithAverageScore();
        // sort
        if (foodDtos.size() > 2) {
            Collections.sort(
                foodDtos, Comparator.comparing(FoodDto::getAverageScore).reversed()
            );
        }
        // get top 10
        return foodDtos.stream().limit(10).collect(Collectors.toList());
    }

    // 최신순 기준 10개 메뉴 FoodDto로 평균점수 포함하여 가져오기
    @Transactional(readOnly = true)
    public List<FoodDto> findTopTenNewest() {
        // get
        List<FoodDto> targetFoodList = findAllWithAverageScore();
        // sort
        if (targetFoodList.size() > 2) {
            Collections.sort(
                targetFoodList, Comparator.comparing(FoodDto::getCreatedAt).reversed()
            );
        }
        // get top 10
        return targetFoodList.stream().limit(10).collect(Collectors.toList());
    }

    // 최신순 기준 10개 메뉴 가져오기
    @Transactional(readOnly = true)
    public List<Food> findFoodTopTenNewest() {
        // get
        List<Food> targetFoodList = findAll();
        // sort
        if (targetFoodList.size() > 2) {
            Collections.sort(
                targetFoodList, Comparator.comparing(Food::getCreatedAt).reversed()
            );
        }
        // get top 10
        return targetFoodList.stream().limit(10).collect(Collectors.toList());
    }

    // 해당 메뉴의 평균 점수를 계산한다
    public double getAverageScoreByFoodId(Long foodId) {        
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





    private List<FoodDto> convertFoodToDto(List<Food> foods) {
        List<FoodDto> foodDtos = new ArrayList<>();
        foods.stream().forEach(food -> {
            FoodDto foodDto = new FoodDto();
            foodDto.setFoodId(food.getFoodId());
            foodDto.setFoodName(food.getName());
            foodDto.setCompany(food.getCompany().getName());
            foodDto.setCategory(food.getCategory().toString());
            foodDto.setFoodImage(food.getImage());
            foodDto.setAverageScore(getAverageScoreByFoodId(food.getFoodId()));
            foodDto.setCreatedAt(food.getCreatedAt());
            foodDtos.add(foodDto);
        });
        return foodDtos;
    }


}