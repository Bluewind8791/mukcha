package com.mukcha.controller;

import com.mukcha.config.dto.LoginUser;
import com.mukcha.config.dto.SessionUser;
import com.mukcha.controller.dto.UserResponseDto;
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
public class CategoryController {

    private final CompanyService companyService;
    private final FoodService foodService;
    private final UserService userService;

    @GetMapping("/category/{categoryName}")
    public String viewCategoryPage(
        @PathVariable String categoryName,
        Model model,
        @LoginUser SessionUser sessionUser
    ) {
        if (sessionUser != null) {
            UserResponseDto user = userService.getSessionUserInfo(sessionUser);
            model.addAttribute("login_user_id", user.getUserId());
            model.addAttribute("login_user_nickname", user.getNickname());
        }
        // 해당 카테고리
        Category thisCategory = Category.valueOf(categoryName);
        model.addAttribute("category", thisCategory);
        // 모든 회사 리스트
        model.addAttribute("companyList", companyService.findAllIntoDto());
        // 해당 카테고리의 메뉴를 런칭한 모든 회사의 메뉴들
        model.addAttribute("foodList", 
            foodService.findAllByCategorySortByCompany(thisCategory)
        );
        return "category/detail";
    }



}
