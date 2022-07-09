package com.mukcha.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

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
@RequestMapping(value = "/admin")
@Tag(name = "Admin Controller",
    description = "ROLE_ADMIN 권한을 가진 사용자가 접근 가능한 관리자 페이지입니다.")
public class AdminController {

    private final CompanyService companyService;
    private final FoodService foodService;
    private final UserService userService;


    @GetMapping({"/", ""})
    @Operation(summary = "관리자 루트 페이지")
    public ModelAndView home(@Parameter(hidden = true) @LoginUser SessionUser sessionUser) {
        Map<String, Object> response = new HashMap<>();
        response.put("_links", // self link 추가
            linkTo(methodOn(AdminController.class).home(sessionUser)).withSelfRel()
        );
        if (sessionUser != null) {
            response.put("loginUser", userService.getSessionUserInfo(sessionUser));
        }
        // 모든 회사 리스트
        response.put("companyList", companyService.findAll());
        // 최근 메뉴 리스트 10개
        response.put("foodList", foodService.findTopTenNewest());
        // 모든 카테고리 리스트
        response.put("categoryList", List.of(Category.values()));
        return new ModelAndView("admin/adminHome", response, HttpStatus.OK);
    }

    @GetMapping("/companies/{companyId}")
    @Operation(summary = "해당 회사 정보 페이지")
    public ModelAndView getCompany(
            @Parameter(hidden = true) @LoginUser SessionUser sessionUser,
            @Parameter(description = "해당 회사 ID", required = true) @PathVariable Long companyId
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put("_links",
            linkTo(methodOn(AdminController.class).getCompany(sessionUser, companyId)).withSelfRel()
        );
        if (sessionUser != null) {
            response.put("loginUser", userService.getSessionUserInfo(sessionUser));
        }
        // 해당 회사의 정보
        response.put("company", companyService.findDtoById(companyId));
        // 해당 회사의 모든 메뉴들
        response.put("foodList", foodService.findDtoAllByCompanyId(companyId));
        // 모든 카테고리 리스트
        response.put("categoryList", List.of(Category.values()));
        return new ModelAndView("admin/adminCompany", response, HttpStatus.OK);
    }

    @GetMapping("/menus")
    @Operation(summary = "모든 메뉴 더보기 페이지")
    public ModelAndView getMenus(@Parameter(hidden = true) @LoginUser SessionUser sessionUser) {
        Map<String, Object> response = new HashMap<>();
        response.put("_links",
            linkTo(methodOn(AdminController.class).getMenus(sessionUser)).withSelfRel()
        );
        if (sessionUser != null) {
            response.put("loginUser", userService.getSessionUserInfo(sessionUser));
        }
        // 모든 카테고리 리스트
        response.put("categoryList", List.of(Category.values()));
        // 모든 메뉴 리스트
        response.put("foodList", foodService.findAll());
        // 모든 회사 리스트
        response.put("companyList", companyService.findAll());
        return new ModelAndView("admin/adminMenuList", response, HttpStatus.OK);
    }

}