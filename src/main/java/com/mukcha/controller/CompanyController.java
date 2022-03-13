package com.mukcha.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
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
        // company Info
        Company targetCompany = companyService.findCompany(companyId).orElseThrow(
            () -> new IllegalArgumentException("해당 회사를 찾을 수 없습니다.")
        );
        model.addAttribute("company", targetCompany);
        // Category List
        List<Category> categoryList = List.of(Category.values());
        model.addAttribute("categoryList", categoryList);

        // Company's food List        
        List<Food> foods = companyService.getFoodListInfo(companyId);
        Map<String, List<Food>> map = new HashMap<>();

        for (Category ctg : Category.values()) {
            List<Food> foodPerCategory = new ArrayList<>();
            foodPerCategory.addAll(foods.stream().filter(f -> f.getCategory() == ctg).collect(Collectors.toList()));
            map.put(ctg.name(), foodPerCategory);
        }
        model.addAttribute("foodList", map);
        
        return "company/detail";
    }


    @GetMapping(value = "/companies")
    public String viewCompanies(Model model) {
        model.addAttribute("companyList", companyService.findAll());
        return "";
    }


}
