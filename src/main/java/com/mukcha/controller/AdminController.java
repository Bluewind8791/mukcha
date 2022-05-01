package com.mukcha.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


// ADMIN 권한 있어야 진입가능
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/admin")
public class AdminController {

    private final CompanyService companyService;
    private final FoodService foodService;
    private final UserService userService;


    // 관리자 루트 페이지
    @GetMapping(value = {"/", ""})
    public String adminHome(Model model, @LoginUser SessionUser sessionUser) {
        if (sessionUser != null) {
            UserResponseDto user = userService.getSessionUserInfo(sessionUser);
            model.addAttribute("login_user_id", user.getUserId());
            model.addAttribute("login_user_nickname", user.getNickname());
            log.info(">>> 관리자 페이지에 진입하였습니다. " + user.getNickname());
        }
        // 모든 회사 리스트
        model.addAttribute("companyList", companyService.findAllIntoDto());
        // 최근 메뉴 리스트 10개
        model.addAttribute("foodList", foodService.findFoodTopTenNewestIntoDto());
        // 모든 카테고리 리스트
        List<Category> categoryList = List.of(Category.values());
        model.addAttribute("categoryList", categoryList);
        return "admin/adminHome";
    }

    // 해당 회사 정보 페이지
    @GetMapping(value = "/companies/{companyId}")
    public String viewAllCompanies(
        Model model,
        @LoginUser SessionUser sessionUser,
        @PathVariable Long companyId
    ) {
        if (sessionUser != null) {
            UserResponseDto user = userService.getSessionUserInfo(sessionUser);
            model.addAttribute("login_user_id", user.getUserId());
            model.addAttribute("login_user_nickname", user.getNickname());
        }
        // 해당 회사의 이름
        model.addAttribute("thisCompanyName", companyService.findCompany(companyId).getName());
        // 해당 회사의 모든 메뉴들
        model.addAttribute("foodList", companyService.findAllFoodsIntoDto(companyId));
        // 모든 카테고리 리스트
        List<Category> categoryList = List.of(Category.values());
        model.addAttribute("categoryList", categoryList);
        return "admin/adminCompany";
    }

    // 모든 메뉴 더보기 페이지
    @GetMapping(value = "/menus")
    public String viewAllMenus(Model model, @LoginUser SessionUser sessionUser) {
        if (sessionUser != null) {
            UserResponseDto user = userService.getSessionUserInfo(sessionUser);
            model.addAttribute("login_user_id", user.getUserId());
            model.addAttribute("login_user_nickname", user.getNickname());
        }
        // 모든 카테고리 리스트
        List<Category> categoryList = List.of(Category.values());
        model.addAttribute("categoryList", categoryList);
        // 모든 메뉴 리스트
        model.addAttribute("foodList", foodService.findAllIntoDto());
        // 모든 회사 리스트
        model.addAttribute("companyList", companyService.findAllIntoDto());
        return "admin/adminMenuList";
    }


}
