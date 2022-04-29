package com.mukcha.controller;

import com.mukcha.controller.dto.CompanyRequestDto;
import com.mukcha.controller.dto.FoodSaveRequestDto;
import com.mukcha.controller.dto.FoodUpdateRequestDto;
import com.mukcha.service.CompanyService;
import com.mukcha.service.FoodService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class AdminApiController {

    private final CompanyService companyService;
    private final FoodService foodService;


    // 회사 추가 메소드
    @PostMapping("/api/companies")
    public Long saveCompany(@RequestBody CompanyRequestDto requestDto) {
        return companyService.save(requestDto);
    }

    // 회사 수정 메소드
    @PutMapping(value = "/api/companies/{companyId}")
    public Long editCompany(
        @PathVariable Long companyId,
        @RequestBody CompanyRequestDto requestDto
    ) {
        return companyService.update(companyId, requestDto);
    }

    // 메뉴 추가 메소드
    @PostMapping(value = "/api/menus")
    public Long saveFood(@RequestBody FoodSaveRequestDto requestDto) {
        return foodService.save(requestDto);
    }

    // 메뉴 수정 메소드
    @PutMapping(value = "/api/menus/{foodId}")
    public Long editFood(@PathVariable Long foodId, @RequestBody FoodUpdateRequestDto requestDto) {
        return foodService.update(foodId, requestDto);
    }

    // 회사 삭제 메소드
    @DeleteMapping(value = "/api/companies/{companyId}")
    public Long deleteCompany(@PathVariable Long companyId) {
        companyService.deleteCompany(companyId);
        return companyId;
    }

    // 메뉴 삭제 메소드
    @DeleteMapping(value = "/api/menus/{foodId}")
    public Long deleteFood(@PathVariable Long foodId) {
        foodService.deleteFood(foodId);
        return foodId;
    }


}
