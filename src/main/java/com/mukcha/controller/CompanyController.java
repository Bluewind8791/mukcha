package com.mukcha.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mukcha.config.dto.LoginUser;
import com.mukcha.config.dto.SessionUser;
import com.mukcha.domain.Category;
import com.mukcha.service.CompanyService;
import com.mukcha.service.FoodService;
import com.mukcha.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/companies")
@Tag(name = "Company Controller")
public class CompanyController {

    private final CompanyService companyService;
    private final UserService userService;
    private final FoodService foodService;

    // VIEW - 각 회사 페이지
    @GetMapping("/{companyId}")
    @Operation(description = "해당 프랜차이즈 회사의 모든 메뉴를 볼 수 있는 페이지입니다.")
    public ModelAndView getCompany(
        @Parameter(description = "회사ID", example = "1") @PathVariable Long companyId,
        @Parameter(hidden = true) @LoginUser SessionUser sessionUser
    ) {
        Map<String, Object> response = new HashMap<>();
        if (sessionUser != null) {
            response.put("loginUser", userService.getSessionUserInfo(sessionUser));
        }
        // 해당 회사 정보
        response.put("company", companyService.findDtoById(companyId));
        // 모든 카테고리
        response.put("categoryList", List.of(Category.values()));
        // 해당 회사의 모든 메뉴 리스트
        response.put("foodList", foodService.findAllFoodsByCategory(companyId));
        return new ModelAndView("company/detail", response, HttpStatus.OK);
    }



}
