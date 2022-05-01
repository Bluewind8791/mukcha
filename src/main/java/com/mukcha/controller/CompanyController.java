package com.mukcha.controller;

import java.util.List;

import com.mukcha.config.dto.LoginUser;
import com.mukcha.config.dto.SessionUser;
import com.mukcha.domain.Category;
import com.mukcha.service.CompanyService;
import com.mukcha.service.FoodService;
import com.mukcha.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final UserService userService;
    private final FoodService foodService;

    // VIEW - 각 회사 페이지
    @GetMapping(value = "/company/{companyId}")
    public String viewCompanyInfo(
        Model model,
        @PathVariable Long companyId,
        @LoginUser SessionUser sessionUser
    ) {
        if (sessionUser != null) {
            model.addAttribute("loginUser", userService.getSessionUserInfo(sessionUser));
        }
        // 해당 회사 정보
        model.addAttribute("company", companyService.findCompany(companyId));
        // 모든 카테고리
        model.addAttribute("categoryList", List.of(Category.values()));
        // 해당 회사의 모든 메뉴 리스트
        model.addAttribute("foodList", foodService.findAllFoodsByCategory(companyId));
        return "company/detail";
    }



}
