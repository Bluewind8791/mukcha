package com.mukcha.service;

import com.mukcha.controller.dto.FoodResponseDto;
import com.mukcha.controller.dto.FoodSaveRequestDto;
import com.mukcha.controller.dto.FoodUpdateRequestDto;
import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.ErrorMessage;
import com.mukcha.domain.Food;
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

import org.springframework.data.jpa.domain.Specification;
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


    public Long save(FoodSaveRequestDto requestDto) {
        Company com = companyRepository.findByName(requestDto.getCompanyName()).orElseThrow(() -> 
            new IllegalArgumentException(ErrorMessage.COMPANY_NOT_FOUND.getMessage() + requestDto.getCompanyName())
        );
        requestDto.setCompanyEntity(com);
        log.info(">>> 메뉴가 생성되었습니다." + requestDto.toString());
        return foodRepository.save(requestDto.toEntity()).getFoodId();
    }

    public boolean update(Long foodId, FoodUpdateRequestDto requestDto) {
        findByFoodId(foodId).update(
            requestDto.getFoodName(), 
            requestDto.getFoodImage(),
            Category.valueOf(requestDto.getCategory())
        );
        Food food = findByFoodId(foodId);
        if (requestDto.equals(food)) {
            log.info(">>> 메뉴 정보가 업데이트 되었습니다. "+requestDto.getFoodName());
            return true;
        } else {
            log.info(ErrorMessage.FAIL_UPDATE.getMessage()+requestDto.toString());
            return false;
        }
    }

    // 해당 메뉴를 삭제한다
    @Transactional
    public boolean deleteById(Long foodId) {
        // 연결된 리뷰 모두 삭제
        reviewRepository.deleteAllByFoodId(foodId);
        log.info(">>> 해당 메뉴의 리뷰가 모두 삭제되었습니다. "+foodId);
        
        // 연결된 회사의 null 처리
        Food targetFood = findByFoodId(foodId);
        Long companyId = targetFood.getCompany().getCompanyId();
        Company company = companyRepository.findById(companyId).orElseThrow(
            () -> new IllegalArgumentException(ErrorMessage.COMPANY_NOT_FOUND.getMessage()+companyId)
        );
        Food originFood = company.getFoods().stream().filter(f -> 
            f.getFoodId().equals(foodId)).findFirst().orElseThrow(() -> 
                new IllegalArgumentException(">>> 테스트중 해당 메뉴를 찾을 수 없습니다.")
        );
        company.deleteFood(originFood);
        companyRepository.save(company);

        // repository 에서 삭제
        foodRepository.deleteById(foodId);
        foodRepository.flush();

        // 삭제 확인
        if (findFoodOr(foodId).isPresent()) {
            log.info(ErrorMessage.FAIL_UPDATE.getMessage()+foodId);
            return false;
        } else {
            log.info(">>> 해당 메뉴를 삭제하였습니다. "+foodId);
            return true;
        }
    }



    @Transactional(readOnly = true)
    public Food findByFoodId(Long foodId) {
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

    // 해당 메뉴의 정보
    @Transactional(readOnly = true)
    public FoodResponseDto findFood(Long foodId) {
        return new FoodResponseDto(findByFoodId(foodId));
    }
    
    // 모든 메뉴를 DTO로 가져오기
    @Transactional(readOnly = true)
    public List<FoodResponseDto> findAll() {
        List<Food> foodList = foodRepository.findAll();
        Collections.reverse(foodList); // 최신순 정렬
        return transDtoList(foodList);
    }

    // 평균 점수를 기준으로 가장 높은 10개의 메뉴를 가져오기
    @Transactional(readOnly = true)
    public List<FoodResponseDto> findTopTenOrderByScore() {
        List<FoodResponseDto> dtos = findAll();
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
        List<Food> allFoodList = foodRepository.findAll();
        // sort
        if (allFoodList.size() > 2) {
            Collections.sort(allFoodList, Comparator.comparing(Food::getCreatedAt).reversed());
        }
        // 가장 최신 10개의 메뉴를 가져오고 dto로 변환
        return transDtoList(allFoodList.stream().limit(10).collect(Collectors.toList()));
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

    // 메뉴 키워드 검색
    public List<FoodResponseDto> findAll(Specification<Food> spec) {
        return transDtoList(foodRepository.findAll(spec));
    }

    // 해당 회사의 모든 메뉴 가져오기
    public List<Food> findAllByCompany(Company company) {
        return foodRepository.findAllByCompany(company);
    }

    public List<FoodResponseDto> findDtoAllByCompanyId(Long companyId) {
        return transDtoList(foodRepository.findAllByCompany( companyRepository.findById(companyId).orElseThrow(() -> 
            new IllegalArgumentException(ErrorMessage.COMPANY_NOT_FOUND.getMessage())
        )));
    }

    // 해당 회사의 모든 메뉴 리스트를 카테고리 별로 매핑하여 가져온다
    public Map<String, List<FoodResponseDto>> findAllFoodsByCategory(Long companyId) {
        List<FoodResponseDto> foods = findDtoAllByCompanyId(companyId);
        Map<String, List<FoodResponseDto>> map = new HashMap<>();
        for (Category ctg : Category.values()) {
            map.put(ctg.name(), foods
                .stream()
                .filter(f -> f.getCategory() == ctg.name())
                .collect(Collectors.toList())
            );
        }
        return map;
    }

    // 해당 회사의 메뉴들 중 해당 메뉴가 있는지 확인
    public boolean isPresentByCompanyAndFoodName(Company company, String menuName) {
        return findAllByCompany(company).stream().anyMatch(f -> f.getName().equals(menuName));
    }

    public Food findByNameAndCompany(Company company, String menuName) {
        return findAllByCompany(company).stream().filter(f -> f.getName().equals(menuName)).findFirst().orElseThrow(
            () -> new IllegalArgumentException(ErrorMessage.MENU_NOT_FOUND.getMessage())
        );
    }

    /* PRIVATE METHODS */
    // Food 리스트를 dto 리스트로 변환한다
    private List<FoodResponseDto> transDtoList(List<Food> foodList) {
        List<FoodResponseDto> dtos = new ArrayList<>();
        foodList.forEach(food -> {
            FoodResponseDto dto = new FoodResponseDto(food);
            dtos.add(dto);
        });
        return dtos;
    }

}