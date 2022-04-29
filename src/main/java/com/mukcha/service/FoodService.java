package com.mukcha.service;

import com.mukcha.controller.dto.FoodDto;
import com.mukcha.controller.dto.FoodSaveRequestDto;
import com.mukcha.controller.dto.FoodUpdateRequestDto;
import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.ErrorMessage;
import com.mukcha.domain.Food;
import com.mukcha.domain.Review;
import com.mukcha.repository.CompanyRepository;
import com.mukcha.repository.FoodRepository;
import com.mukcha.repository.ReviewRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final CompanyRepository companyRepository;
    private final ReviewRepository reviewRepository;
    private final CompanyService companyService;


    // 음식을 생성한다.
    public Food save(Food food) {
        log.info(">>> 메뉴가 생성되었습니다." + food.toString());
        return foodRepository.save(food);
    }

    public Long save(FoodSaveRequestDto requestDto) {
        Company com = companyRepository.findByName(requestDto.getCompanyName()).orElseThrow(() -> 
        new IllegalArgumentException(ErrorMessage.COMPANY_NOT_FOUND.getMessage())
        );
        requestDto.setCompanyEntity(com);
        log.info(">>> 메뉴가 생성되었습니다." + requestDto.toString());
        return foodRepository.save(requestDto.toEntity()).getFoodId();
    }

    public Long update(Long foodId, FoodUpdateRequestDto requestDto) {
        findFood(foodId).update(
            requestDto.getFoodName(), 
            requestDto.getFoodImage(),
            Category.valueOf(requestDto.getCategory())
        );
        return foodId;
    }

    // 음식 회사를 수정한다
    public void editFoodCompany(Long foodId, String companyName) {
        // 해당 회사와 메뉴가 존재할 때만 메소드 실행
        companyRepository.findByName(companyName).ifPresent(com -> {
            findFoodOr(foodId).ifPresent(f -> {
                f.setCompany(com);
                save(f);
                log.info("해당 메뉴의 회사를 변경하였습니다."+f.toString());
            });
        });
    }

    // 음식을 삭제한다
    @Transactional
    public void deleteFood(Long foodId) {
        Food targetFood = findFood(foodId);
        // 연결된 리뷰 모두 삭제
        reviewRepository.deleteAllByFoodId(foodId);
        // 연결된 회사의 null 처리
        companyService.deleteFood(targetFood.getCompany().getCompanyId(), foodId);
        foodRepository.delete(targetFood);
    }


    /* FIND methods */
    // 해당 회사의 메뉴 이름과 같은 메뉴가 있는지
    public boolean isPresentFindByFoodNameAndCompanyName(String foodName, String companyName) {
        boolean isPresent;
        if (findByNameOr(foodName).isPresent()) {
            if (findByNameOr(foodName).get().getCompany().getName().equals(companyName))
                isPresent = true;
        }
        isPresent = false;
        return isPresent;
    }

    public Food findFood(Long foodId) {
        return foodRepository.findById(foodId).orElseThrow(() -> 
            new IllegalArgumentException(ErrorMessage.MENU_NOT_FOUND.getMessage() + foodId)
        );
    }

    public Optional<Food> findFoodOr(Long foodId) {
        return foodRepository.findById(foodId);
    }

    public Optional<Food> findByNameOr(String foodName) {
        return foodRepository.findByName(foodName);
    }

    public Food findByName(String foodName) {
        return foodRepository.findByName(foodName).orElseThrow(() -> 
            new IllegalArgumentException(ErrorMessage.MENU_NOT_FOUND.getMessage() + foodName)
        );
    }

    public List<String> categories() {
        return foodRepository.findAllCategories();
    }

    public List<Food> findAllByCategory(Category category) {
        return foodRepository.findAllByCategory(category);
    }

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
        foodDto.setCompanyName(food.getCompany().getName());
        foodDto.setCompanyId(food.getCompany().getCompanyId());
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

    // 각 회사별 메뉴들을 카테고리 별로 가져온다
    public Map<String, List<FoodDto>> findAllByCategorySortByCompany(Category category) {
        Map<String, List<FoodDto>> foodMap = new HashMap<>();
        for (Company company : companyRepository.findAll()) {
            List<Food> foods = foodRepository.findAllByCompanyAndCategory(company, category);
            List<FoodDto> foodDtoList = new ArrayList<>();
            foods.stream().forEach(food -> {
                FoodDto foodDto = new FoodDto();
                foodDto.setFoodId(food.getFoodId());
                foodDto.setFoodName(food.getName());
                foodDto.setCompanyName(food.getCompany().getName());
                foodDto.setFoodImage(food.getImage());
                foodDtoList.add(foodDto);
            });
            foodMap.put(company.getName(), foodDtoList);
        }
        return foodMap;
    }


    private List<FoodDto> convertFoodToDto(List<Food> foods) {
        List<FoodDto> foodDtos = new ArrayList<>();
        foods.stream().forEach(food -> {
            FoodDto foodDto = new FoodDto();
            foodDto.setFoodId(food.getFoodId());
            foodDto.setFoodName(food.getName());
            foodDto.setCompanyName(food.getCompany().getName());
            foodDto.setCategory(food.getCategory().toString());
            foodDto.setFoodImage(food.getImage());
            foodDto.setAverageScore(getAverageScoreByFoodId(food.getFoodId()));
            foodDto.setCreatedAt(food.getCreatedAt());
            foodDto.setCompanyId(food.getCompany().getCompanyId());
            foodDtos.add(foodDto);
        });
        return foodDtos;
    }


}