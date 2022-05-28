package com.mukcha.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mukcha.config.dto.LoginUser;
import com.mukcha.config.dto.SessionUser;
import com.mukcha.controller.dto.SessionUserResponseDto;
import com.mukcha.domain.Category;
import com.mukcha.service.CompanyService;
import com.mukcha.service.FoodService;
import com.mukcha.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


// ADMIN 권한 있어야 진입가능
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin")
public class AdminController {

    private final CompanyService companyService;
    private final FoodService foodService;
    private final UserService userService;


    // 관리자 루트 페이지
    @GetMapping(value = {"/", ""})
    public ModelAndView home(@LoginUser SessionUser sessionUser) {
        Map<String, Object> response = new HashMap<>();
        if (sessionUser != null) {
            SessionUserResponseDto user = userService.getSessionUserInfo(sessionUser);
            response.put("loginUser", user);
            log.info(">>> 관리자 페이지에 진입하였습니다. "+user.getUserEmail());
        }
        // 모든 회사 리스트
        response.put("companyList", companyService.findAll());
        // 최근 메뉴 리스트 10개
        response.put("foodList", foodService.findTopTenNewest());
        // 모든 카테고리 리스트
        response.put("categoryList", List.of(Category.values()));
        return new ModelAndView("admin/adminHome", response, HttpStatus.OK);
    }

    // 해당 회사 정보 페이지
    @GetMapping(value = "/companies/{companyId}")
    public ModelAndView getCompany(@LoginUser SessionUser sessionUser, @PathVariable Long companyId) {
        Map<String, Object> response = new HashMap<>();
        if (sessionUser != null) {
            response.put("loginUser", userService.getSessionUserInfo(sessionUser));
        }
        // 해당 회사의 정보
        response.put("company", companyService.findCompany(companyId));
        // 해당 회사의 모든 메뉴들
        response.put("foodList", foodService.findAllByCompanyId(companyId));
        // 모든 카테고리 리스트
        response.put("categoryList", List.of(Category.values()));
        return new ModelAndView("admin/adminCompany", response, HttpStatus.OK);
    }

    // 모든 메뉴 더보기 페이지
    @GetMapping(value = "/menus")
    public ModelAndView getMenus(@LoginUser SessionUser sessionUser) {
        Map<String, Object> response = new HashMap<>();
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