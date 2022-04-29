package com.mukcha.controller;


import javax.servlet.http.HttpServletRequest;

import com.mukcha.controller.dto.CompanyRequestDto;
import com.mukcha.controller.dto.FoodSaveRequestDto;
import com.mukcha.controller.dto.FoodUpdateRequestDto;
import com.mukcha.service.CompanyService;
import com.mukcha.service.FoodService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/admin")
public class AdminApiController {

    private final CompanyService companyService;
    private final FoodService foodService;


    // 회사 추가 메소드
    @PostMapping("/api/companies")
    public String saveCompany(@ModelAttribute CompanyRequestDto requestDto, HttpServletRequest request) {
        companyService.save(requestDto);
        String referer = request.getHeader("Referer"); // 헤더에서 이전 페이지를 읽는다.
        return "redirect:"+ referer; // 이전 페이지로 리다이렉트
    }

    // 회사 수정 메소드
    @PutMapping(value = "/api/companies/{companyId}")
    public String editCompany(
        @PathVariable Long companyId,
        @ModelAttribute CompanyRequestDto requestDto,
        HttpServletRequest request
    ) {
        companyService.update(companyId, requestDto);
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    // 메뉴 추가 메소드
    @PostMapping(value = "/api/menus")
    public String saveFood(@ModelAttribute FoodSaveRequestDto requestDto, HttpServletRequest request) {
        System.out.println(">>> " + requestDto.getFoodName()+requestDto.getFoodImage()+requestDto.getCategory()+requestDto.getCompanyName());
        foodService.save(requestDto);
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    // 메뉴 수정 메소드
    @PutMapping(value = "/api/menus/{foodId}")
    public String editFood(
        @PathVariable Long foodId,
        @ModelAttribute FoodUpdateRequestDto requestDto,
        HttpServletRequest request
    ) {
        foodService.update(foodId, requestDto);
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    // 회사 삭제 메소드
    @DeleteMapping(value = "/api/companies/{companyId}")
    public String deleteCompany(@PathVariable Long companyId) {
        companyService.deleteCompany(companyId);
        return "redirect:/admin";
    }

    // 메뉴 삭제 메소드
    @DeleteMapping(value = "/api/menus/{foodId}")
    public String deleteFood(@PathVariable Long foodId, HttpServletRequest request) {
        foodService.deleteFood(foodId);
        // return "redirect:/admin";
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }


}
