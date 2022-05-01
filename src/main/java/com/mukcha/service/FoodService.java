package com.mukcha.service;

import com.mukcha.controller.dto.FoodResponseDto;
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


    public Food save(Food food) {
        log.info(">>> 메뉴가 생성되었습니다." + food.toString());
        return foodRepository.save(food);
    }

    public Long save(FoodSaveRequestDto requestDto) {
        Company com = companyRepository.findByName(requestDto.getCompanyName()).orElseThrow(() -> 
            new IllegalArgumentException(ErrorMessage.COMPANY_NOT_FOUND.getMessage() + requestDto.getCompanyName())
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
        log.info(">>> 메뉴 정보가 업데이트 되었습니다." + foodId);
        return foodId;
    }

    // 해당 메뉴의 회사를 수정한다
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

    // 해당 메뉴를 삭제한다
    @Transactional
    public void deleteFood(Long foodId) {
        Food targetFood = findFood(foodId);
        // 연결된 리뷰 모두 삭제
        reviewRepository.deleteAllByFoodId(foodId);
        // 연결된 회사의 null 처리
        companyService.deleteFood(targetFood.getCompany().getCompanyId(), foodId);
        foodRepository.delete(targetFood);
        log.info("해당 메뉴를 삭제하였습니다." + foodId);
    }

    // 해당 회사의 메뉴 이름과 같은 메뉴가 있는지 확인
    public boolean isPresentFindByFoodNameAndCompanyName(String foodName, String companyName) {
        boolean isPresent;
        if (findByNameOr(foodName).isPresent()) {
            if (findByNameOr(foodName).get().getCompany().getName().equals(companyName))
                isPresent = true;
        }
        isPresent = false;
        return isPresent;
    }

    @Transactional(readOnly = true)
    public Food findFood(Long foodId) {
        return foodRepository.findById(foodId).orElseThrow(() -> 
            new IllegalArgumentException(ErrorMessage.MENU_NOT_FOUND.getMessage() + foodId)
        );
    }

    @Transactional(readOnly = true)
    public Optional<Food> findFoodOr(Long foodId) {
        return foodRepository.findById(foodId);
    }

    @Transactional(readOnly = true)
    public Optional<Food> findByNameOr(String foodName) {
        return foodRepository.findByName(foodName);
    }

    @Transactional(readOnly = true)
    public Food findByName(String foodName) {
        return foodRepository.findByName(foodName).orElseThrow(() -> 
            new IllegalArgumentException(ErrorMessage.MENU_NOT_FOUND.getMessage() + foodName)
        );
    }

    @Transactional(readOnly = true)
    public List<String> categories() {
        return foodRepository.findAllCategories();
    }

    @Transactional(readOnly = true)
    public List<Food> findAllByCategory(Category category) {
        return foodRepository.findAllByCategory(category);
    }

    @Transactional(readOnly = true)
    public List<Food> findAll() {
        return foodRepository.findAll();
    }

    // 최신순 기준 10개 메뉴 Dto로 가져오기
    @Transactional(readOnly = true)
    public List<FoodResponseDto> findFoodTopTenNewestIntoDto() {
        List<Food> targetFoodList = findAll();
        if (targetFoodList.size() > 2) { // sort
            Collections.sort(
                targetFoodList, Comparator.comparing(Food::getCreatedAt).reversed()
            );
        }
        // get top 10
        List<Food> topTenFoods = targetFoodList.stream().limit(10).collect(Collectors.toList());
        List<FoodResponseDto> dtos = new ArrayList<>();
        topTenFoods.forEach(food -> {
            FoodResponseDto dto = new FoodResponseDto(food);
            dtos.add(dto);
        });
        return dtos;
    }

    // 해당 메뉴의 정보
    @Transactional(readOnly = true)
    public FoodResponseDto findFoodIntoDto(Long foodId) {
        return new FoodResponseDto(findFood(foodId));
    }

    // 모든 메뉴를 DTO로 가져오기
    public List<FoodResponseDto> findAllIntoDto() {
        List<Food> foodList = findAll();
        Collections.reverse(foodList); // 최신순 정렬
        return transDtoList(foodList);
    }

    // 평균 점수를 기준으로 가장 높은 10개의 메뉴를 가져오기
    @Transactional(readOnly = true)
    public List<FoodResponseDto> findTopTenOrderByScore() {
        List<FoodResponseDto> dtos = findAllIntoDto();
        // sort
        if (dtos.size() > 2) {
            Collections.sort(
                dtos, Comparator.comparing(FoodResponseDto::getAverageScore).reversed()
            );
        }
        // get top 10
        return dtos.stream().limit(10).collect(Collectors.toList());
    }

    // 가장 최신의 10개 메뉴를 가져온다
    @Transactional(readOnly = true)
    public List<FoodResponseDto> findTopTenNewest() {
        List<Food> allFoodList = findAll();
        // sort
        if (allFoodList.size() > 2) {
            Collections.sort(allFoodList, Comparator.comparing(Food::getCreatedAt).reversed());
        }
        // 가장 최신 10개의 메뉴를 가져오고 dto로 변환
        return transDtoList(allFoodList.stream().limit(10).collect(Collectors.toList()));
    }

    // method - Food 리스트를 dto 리스트로 변환한다
    private List<FoodResponseDto> transDtoList(List<Food> foodList) {
        List<FoodResponseDto> dtos = new ArrayList<>();
        foodList.forEach(food -> {
            FoodResponseDto dto = new FoodResponseDto(food);
            dtos.add(dto);
        });
        return dtos;
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
    public Map<String, List<FoodResponseDto>> findAllByCategorySortByCompany(Category category) {
        Map<String, List<FoodResponseDto>> foodMap = new HashMap<>();
        for (Company company : companyRepository.findAll()) {
            List<Food> foods = foodRepository.findAllByCompanyAndCategory(company, category);
            List<FoodResponseDto> foodDtoList = new ArrayList<>();
            foods.stream().forEach(food -> {
                FoodResponseDto foodDto = new FoodResponseDto(food);
                foodDtoList.add(foodDto);
            });
            foodMap.put(company.getName(), foodDtoList);
        }
        return foodMap;
    }



}