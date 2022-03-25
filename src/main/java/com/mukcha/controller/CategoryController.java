package com.mukcha.controller;

import java.util.List;

import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.service.CompanyService;
import com.mukcha.service.FoodService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CompanyService companyService;
    private final FoodService foodService;

    @GetMapping("/category/{categoryName}")
    public String viewCategoryPage(
        @PathVariable String categoryName,
        Model model
    ) {
        // 카테고리 이름으로 카테고리를 찾아서 넘겨준다
        Category thisCategory = Category.valueOf(categoryName);
        model.addAttribute("category", thisCategory);
        // 모든 회사 리스트를 넘겨준다
        List<Company> companyList = companyService.findAll();
        model.addAttribute("companyList", companyList);
        // 각 회사별 메뉴들을 카테고리 별로 분류하여 넘겨준다
        model.addAttribute("foodList", 
            foodService.findAllByCategorySortByCompany(thisCategory)
        );
        return "category/detail";
    }



}
