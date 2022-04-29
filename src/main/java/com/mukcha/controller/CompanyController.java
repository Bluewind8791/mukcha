package com.mukcha.controller;

import java.util.List;

import com.mukcha.domain.Category;
import com.mukcha.service.CompanyService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    // VIEW - 각 회사 페이지
    @GetMapping(value = "/company/{companyId}")
    public String viewCompanyInfo(
        @PathVariable Long companyId,
        Model model
    ) {
        // 해당 회사 정보
        model.addAttribute("company", companyService.findCompanyIntoDto(companyId));
        // 모든 카테고리
        model.addAttribute("categoryList", List.of(Category.values()));
        // 해당 회사의 모든 메뉴 리스트
        model.addAttribute("foodList", companyService.findAllFoodsByCategory(companyId));
        return "company/detail";
    }



}
