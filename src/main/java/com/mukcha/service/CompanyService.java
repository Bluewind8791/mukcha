package com.mukcha.service;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.mukcha.domain.Company;
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
        log.info(">>> 회사 <"+company.getName()+">를 생성합니다.");
        return companyRepository.save(company);
    }

    @Transactional(readOnly = true)
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Company> findCompany(Long companyId) {
        return companyRepository.findById(companyId);
    }

    @Transactional(readOnly = true)
    public Optional<Company> findByName(String companyName) {
        return companyRepository.findByName(companyName);
    }

    // 가장 최신의 10개 회사 가져오기
    @Transactional(readOnly = true)
    public List<Company> findCompanyTopTenNewest() {
        // get
        List<Company> targetCompanyList = findAll();
        // sort
        if (targetCompanyList.size() > 2) {
            Collections.sort(
                targetCompanyList, Comparator.comparing(Company::getCreatedAt).reversed()
            );
        }
        // get top 10
        if (targetCompanyList.size() < 10) {
            return targetCompanyList;
        } else {
            return targetCompanyList.stream().limit(10).collect(Collectors.toList());
        }
    }

    // 해당 회사의 모든 메뉴 가져오기
    public List<Food> getFoodListInfo(Long companyId) {
        Company company = findCompany(companyId).orElseThrow(
            () -> new IllegalArgumentException("해당 회사를 찾을 수 없습니다.")
        );
        return foodRepository.findAllByCompany(company);
    }





    // 해당 회사의 메뉴를 추가한다
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



    // DELETE company
    public void deleteCompany(Long companyId) {
        Company targetCompany = findCompany(companyId).orElseThrow(
            () -> new IllegalArgumentException("해당 회사를 찾을 수 없습니다.")
        );
        // 음식과 회사의 연관관계를 끊는다
        setNullcompanysFood(targetCompany);
        companyRepository.delete(targetCompany);
        if (findCompany(companyId).isPresent()) {
            throw new IllegalArgumentException("삭제에 실패하였습니다.");
        }
    }

    // company's food setCompany NULL
    private void setNullcompanysFood(Company company) {
        // 해당 회사의 모든 음식을 찾는다
        List<Food> originFoods = foodRepository.findAllByCompany(company);
        if (originFoods.isEmpty()) {
            return;
        }
        // 만약 회사에 메뉴들이 존재한다면
        originFoods.forEach(f -> f.setCompany(null));
    }


    public void editCompanyLogo(Long companyId, String companyLogo) {
        findCompany(companyId).ifPresent(com -> {
            companyRepository.updateCompanyImage(companyId, companyLogo);
        });
    }

    public void editCompanyName(Long companyId, String companyName) {
        findCompany(companyId).ifPresent(com -> {
            companyRepository.updateCompanyName(companyId, companyName);
        });
    }




}
/*
    // 해당 회사의 해당 메뉴를 삭제한다.
    public void companyRemoveFood(Long companyId, Food food) {
        // id 로 회사를 찾는다
        Company company = companyRepository.findById(companyId).orElseThrow(
            () -> new IllegalArgumentException("해당 회사를 찾을 수 없습니다.")
        );
        List<Food> originFoods = company.getFoods();
        // 기존 food 리스트에 삭제하려는 food가 들어있다면
        if (originFoods.contains(food)) {
            originFoods.remove(food);
            companyRepository.save(company);
        }
    }
*/
