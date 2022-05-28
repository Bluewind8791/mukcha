package com.mukcha.controller;

import java.util.HashMap;
import java.util.Map;

import com.mukcha.config.dto.LoginUser;
import com.mukcha.config.dto.SessionUser;
import com.mukcha.domain.Category;
import com.mukcha.service.CompanyService;
import com.mukcha.service.FoodService;
import com.mukcha.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CompanyService companyService;
    private final FoodService foodService;
    private final UserService userService;

    @GetMapping("/category/{categoryName}")
    public ModelAndView viewCategoryPage(
        @PathVariable String categoryName,
        @LoginUser SessionUser sessionUser
    ) {
        Map<String, Object> response = new HashMap<>();
        if (sessionUser != null) {
            response.put("loginUser", userService.getSessionUserInfo(sessionUser));
        }
        response.put("companyList", companyService.findAll()); // 모든 회사 리스트
        Category thisCategory = Category.valueOf(categoryName);
        response.put("category", thisCategory); // 해당 카테고리
        // 해당 카테고리의 메뉴를 런칭한 모든 회사의 메뉴들
        response.put("foodList", foodService.findAllByCategorySortByCompany(thisCategory));
        return new ModelAndView("category/detail", response, HttpStatus.OK);
    }

}
