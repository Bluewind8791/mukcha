package com.mukcha.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mukcha.controller.dto.CompanyRequestDto;
import com.mukcha.controller.dto.CompanyResponseDto;
import com.mukcha.domain.Company;
import com.mukcha.domain.ErrorMessage;
import com.mukcha.domain.Food;
import com.mukcha.repository.CompanyRepository;
import com.mukcha.repository.FoodRepository;

import org.springframework.data.jpa.domain.Specification;
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


    public Company save(Company company) {
        log.info(">>> 회사 <"+company.getName()+">를 생성합니다. "+company.toString());
        return companyRepository.save(company);
    }

    public boolean save(CompanyRequestDto requestDto) {
        Long companyId = companyRepository.save(requestDto.toEntity()).getCompanyId();
        if (companyRepository.findById(companyId).isPresent()) {
            log.info(">>> 회사 <"+requestDto.getCompanyName()+">가 생성되었습니다. "+requestDto.toString());
            return true;
        } else {
            log.info(ErrorMessage.FAIL_SAVE+requestDto.toString());
            return false;
        }
    }

    public boolean update(Long companyId, CompanyRequestDto requestDto) {
        String updateName = requestDto.getCompanyName();
        String updateLogo = requestDto.getCompanyLogo();
        Company company = findById(companyId);
        company.update(updateName, updateLogo);
        Company com = findById(companyId);
        if (com.getName() == updateName && com.getImage() == updateLogo) {
            return true;
        }
        return false;
    }

    // 회사 삭제
    public boolean deleteCompany(Long companyId) {
        Company targetCompany = findById(companyId);
        // 해당 회사의 모든 음식을 찾는다
        List<Food> originFoods = foodRepository.findAllByCompany(targetCompany);
        if (!originFoods.isEmpty()) {
            // 만약 회사에 메뉴들이 존재한다면 연관관계를 끊는다
            originFoods.forEach(f -> f.setCompanyNull());
        }
        companyRepository.delete(targetCompany);
        if (!findByIdOr(companyId).isPresent()) {
            log.info(">>> 해당 회사가 성공적으로 삭제되었습니다.");
            return true;
        } else {
            log.info(">>> 해당 회사 삭제에 실패하였습니다.");
            return false;
        }
    }



    /** FIND METHODS */

    // 회사 검색 키워드
    public List<CompanyResponseDto> findAll(Specification<Company> spec) {
        return transDtoList(companyRepository.findAll(spec));
    }

    @Transactional(readOnly = true)
    public List<CompanyResponseDto> findAll() {
        List<Company> companies = companyRepository.findAll();
        List<CompanyResponseDto> companyDtoList = new ArrayList<>();
        companies.stream().forEach(com -> {
            CompanyResponseDto responseDto = new CompanyResponseDto(com);
            companyDtoList.add(responseDto);
        });
        return companyDtoList;
    }

    @Transactional(readOnly = true)
    public Company findById(Long companyId) {
        return companyRepository.findById(companyId).orElseThrow(() -> 
            new IllegalArgumentException(ErrorMessage.COMPANY_NOT_FOUND.getMessage() + companyId)
        );
    }

    @Transactional(readOnly = true)
    public Optional<Company> findByIdOr(Long companyId) {
        return companyRepository.findById(companyId);
    }

    @Transactional(readOnly = true)
    public CompanyResponseDto findDtoById(Long companyId) {
        Company company = companyRepository.findById(companyId).orElseThrow(() -> 
            new IllegalArgumentException(ErrorMessage.COMPANY_NOT_FOUND.getMessage() + companyId)
        );
        return new CompanyResponseDto(company);
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


    // METHOD - company list를 dto list로 변환
    private List<CompanyResponseDto> transDtoList(List<Company> companyList) {
        List<CompanyResponseDto> dtos = new ArrayList<>();
        companyList.forEach(food -> {
            CompanyResponseDto dto = new CompanyResponseDto(food);
            dtos.add(dto);
        });
        return dtos;
    }

}
