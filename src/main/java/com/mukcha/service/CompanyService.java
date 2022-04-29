package com.mukcha.service;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.mukcha.controller.dto.CompanyRequestDto;
import com.mukcha.domain.Company;
import com.mukcha.domain.ErrorMessage;
import com.mukcha.domain.Food;
import com.mukcha.repository.CompanyRepository;
import com.mukcha.repository.FoodRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final FoodRepository foodRepository;


    // 회사 추가
    public Company save(Company company) {
        log.info(">>> 회사 <"+company.getName()+">를 생성합니다." + company.toString());
        return companyRepository.save(company);
    }

    public Long save(CompanyRequestDto requestDto) {
        log.info(">>> 회사 <"+requestDto.getCompanyName()+">를 생성합니다." + requestDto.toString());
        return companyRepository.save(requestDto.toEntity()).getCompanyId();
    }

    // 회사 정보 수정
    @Transactional
    public Long update(Long companyId, CompanyRequestDto requestDto) {
        Company company = findCompany(companyId);
        company.update(requestDto.getCompanyName(), requestDto.getCompanyLogo());
        return companyId;
    }

    // 회사 삭제
    public void deleteCompany(Long companyId) {
        Company targetCompany = findCompany(companyId);
        // 삭제하려는 회사의 모든 음식을 찾고 연관관계를 끊는다
        setNullcompanysFood(targetCompany);
        companyRepository.delete(targetCompany);
    }

    // 회사의 모든 음식을 찾고 연관관계를 끊는다
    private void setNullcompanysFood(Company company) {
        // 해당 회사의 모든 음식을 찾는다
        List<Food> originFoods = foodRepository.findAllByCompany(company);
        if (originFoods.isEmpty()) {
            return;
        }
        // 만약 회사에 메뉴들이 존재한다면
        originFoods.forEach(f -> f.setCompanyNull());
    }


    @Transactional(readOnly = true)
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Company findCompany(Long companyId) {
        return companyRepository.findById(companyId).orElseThrow(() -> 
            new IllegalArgumentException(ErrorMessage.COMPANY_NOT_FOUND.getMessage() + companyId)
        );
    }

    @Transactional(readOnly = true)
    public Optional<Company> findCompanyOr(Long companyId) {
        return companyRepository.findById(companyId);
    }

    @Transactional(readOnly = true)
    public Company findByName(String companyName) {
        return companyRepository.findByName(companyName).orElseThrow(() -> 
            new IllegalArgumentException(ErrorMessage.COMPANY_NOT_FOUND.getMessage() + companyName)
        );
    }

    @Transactional(readOnly = true)
    public Optional<Company> findByNameOr(String companyName) {
        return companyRepository.findByName(companyName);
    }

    // 가장 최신의 10개 회사 가져오기 for admin controller
    @Transactional(readOnly = true)
    public List<Company> findCompanyTopTenNewest() {
        // get
        List<Company> companies = findAll();
        // sort
        if (companies.size() > 2) {
            companies.stream().sorted(
                Comparator.comparing(Company::getUpdatedAt)).collect(Collectors.toList()
            );
        }
        // get top 10
        if (companies.size() > 10) {
            return companies.stream().limit(10).collect(Collectors.toList());
        } else {
            return companies;
        }
    }

    // 해당 회사의 모든 메뉴 가져오기
    public List<Food> getFoodList(Long companyId) {
        Company company = findCompany(companyId);
        return foodRepository.findAllByCompany(company);
    }

    // 해당 회사의 메뉴를 추가한다
    public void companyAddFood(Long companyId, Food food) {
        // find company
        companyRepository.findById(companyId).ifPresentOrElse(company -> {
            // company foods 에 food 가 없다면
            if (!company.getFoods().contains(food)) {
                company.addFood(food);
                save(company);
            }
        }, () -> new IllegalArgumentException("해당 회사를 찾을 수 없습니다."));
    }

    public void editCompanyLogo(Long companyId, String companyLogo) {
        Company company = findCompany(companyId);
        company.editCompanyImageUrl(companyLogo);
    }

    public void editCompanyName(Long companyId, String companyName) {
        Company company = findCompany(companyId);
        company.editCompanyName(companyName);
    }

    // DTO로 변환하여 모든 회사의 이름만 가져오기
    public List<CompanyRequestDto> findAllCompanyName() {
        List<CompanyRequestDto> companyDtoList = new ArrayList<>();
        List<Company> companies = findAll();
        companies.stream().forEach(com -> {
            CompanyRequestDto companyDto = new CompanyRequestDto();
            companyDto.setCompanyName(com.getName());
            companyDtoList.add(companyDto);
        });
        return companyDtoList;
    }

    public void deleteFood(Long companyId, Long foodId) {
        // 회사를 찾는다
        Company company = findCompany(companyId);
        Food targetFood = foodRepository.findById(foodId).orElseThrow(
            () -> new IllegalArgumentException(ErrorMessage.MENU_NOT_FOUND.getMessage()+foodId)
        );
        targetFood.setCompanyNull();
        List<Food> originFoods = company.getFoods();
        // 기존 food 리스트에 삭제하려는 food가 들어있다면
        if (originFoods.contains(targetFood)) {
            originFoods.remove(targetFood);
            companyRepository.save(company);
        }
    }


}
