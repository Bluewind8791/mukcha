package com.mukcha.controller;

import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@RestController
@RequiredArgsConstructor
@Tag(name = "Category Controller")
public class CategoryController {

    private final CompanyService companyService;
    private final FoodService foodService;
    private final UserService userService;

    @GetMapping("/category/{categoryName}")
    @Operation(description = "해당 카테고리의 모든 메뉴를 볼 수 있는 페이지입니다.")
    public ModelAndView categoryPage(
        @Parameter(description = "카테고리명", example = "CHICKEN") @PathVariable String categoryName,
        @Parameter(hidden = true) @LoginUser SessionUser sessionUser
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put("_links",
            linkTo(methodOn(CategoryController.class).categoryPage(categoryName, sessionUser)).withSelfRel()
        );
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
