package com.mukcha.controller;

import java.util.Collections;
import java.util.List;

import com.mukcha.controller.dto.CompanyDto;
import com.mukcha.controller.dto.FoodDto;
import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.service.CompanyService;
import com.mukcha.service.FoodService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


// ROLE_ADMIN 권한 있어야 진입가능
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/admin")
public class AdminController {

    private final CompanyService companyService;
    private final FoodService foodService;


    // VIEW - ROOT PAGE
    @GetMapping(value = {"/", ""})
    public String adminHome(Model model) {
        log.info("관리자 페이지에 진입하였습니다.");
        model.addAttribute("foodList", foodService.findFoodTopTenNewest());
        model.addAttribute("companyList", companyService.findCompanyTopTenNewest());
        // Category List
        List<Category> categoryList = List.of(Category.values());
        model.addAttribute("categoryList", categoryList);
        return "admin/adminHome";
    }


    // VIEW - 모든 메뉴 리스트
    @GetMapping(value = "/menus")
    public String viewAllMenus(Model model) {
        List<Category> categoryList = List.of(Category.values());
        model.addAttribute("categoryList", categoryList);
        List<Food> foodList = foodService.findAll();
        Collections.reverse(foodList);
        model.addAttribute("foodList", foodList);
        model.addAttribute("companyList", companyService.findAll());
        return "admin/adminMenuList";
    }


    // VIEW - 모든 회사 리스트
    @GetMapping(value = "/companies")
    public String viewAllCompanies(Model model) {
        model.addAttribute("companyList", companyService.findAll());
        return "admin/adminCompanyList";
    }


    // 회사 추가하기
    @PostMapping(value = "/company")
    public String addCompany(
            @ModelAttribute CompanyDto companyDto,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        Company company = Company.builder()
                            .name(companyDto.getCompanyName())
                            .image(companyDto.getCompanyLogo())
                            .build()
        ;
        Company savedCompany = companyService.save(company);
        log.info("회사가 생성되었습니다." + savedCompany.toString());
        return "redirect:/admin/";
    }


    // 메뉴(음식) 추가
    @PostMapping(value = "/menu")
    public String addMenu(
            @ModelAttribute FoodDto foodDto,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        Food food = Food.builder()
                        .name(foodDto.getFoodName())
                        .image(foodDto.getFoodImage())
                        .category(transCategory(foodDto.getCategory()))
                        .company(transCompany(foodDto.getCompanyName()))
                        .build()
        ;
        Food savedFood = foodService.save(food);
        log.info("메뉴가 생성되었습니다." + savedFood.toString());
        return "redirect:/admin/";
    }


    // 회사를 삭제
    @DeleteMapping(value = "/companies/delete/{companyId}")
    public String deleteCompany(@PathVariable Long companyId) {
        String name = companyService.findCompany(companyId).get().getName();
        companyService.deleteCompany(companyId);
        if (companyService.findCompany(companyId).isPresent()) {
            log.info("회사 <"+name+"> 의 삭제에 실패하였습니다..");
        } else {
            log.info("회사 <"+name+"> 이 삭제되었습니다.");
        }
        return "redirect:/admin/companies/";
    }


    // 음식을 삭제
    @DeleteMapping(value = "/menus/delete/{foodId}")
    public String deleteFood(@PathVariable Long foodId) {
        String name = foodService.findFood(foodId).get().getName();
        foodService.deleteFood(foodId);
        if (foodService.findFood(foodId).isPresent()) {
            log.info("메뉴 <"+name+"> 의 삭제가 실패하였습니다.");
        } else {
            log.info("메뉴 <"+name+"> 가 삭제되었습니다.");
        }
        return "redirect:/admin/menus/";
    }


    // 회사 수정
    @PostMapping(value = "/companies/edit/{companyId}")
    public String editCompany(
        @PathVariable Long companyId,
        CompanyDto companyDto
    ) {
        companyService.editCompanyName(companyId, companyDto.getCompanyName());
        companyService.editCompanyLogo(companyId, companyDto.getCompanyLogo());
        return "redirect:/admin/companies/";
    }


    // 메뉴 수정
    @PostMapping(value = "/menus/edit/{foodId}")
    public String editFood(
        @PathVariable Long foodId,
        FoodDto foodDto
    ) {
        foodService.editFoodName(foodId, foodDto.getFoodName());
        foodService.editFoodImage(foodId, foodDto.getFoodImage());
        if (foodDto.getCompanyName() != "") {
            foodService.editFoodCompany(foodId, foodDto.getCompanyName());
        }
        if (foodDto.getCategory() != "") {
            foodService.editFoodCategory(foodId, transCategory(foodDto.getCategory()));
        }
        return "redirect:/admin/menus/";
    }



    private Category transCategory(String category) {
        String upperCategory = category.toUpperCase();

        Category arr[] = Category.values();

        for (Category c : arr) {
            // 동일한 카테고리가 존재한다면
            if (upperCategory.equals(c.toString())) {
                return c;
            }
        }
        // 해당 카테고리가 존재하지 않을 시
        return null;
    }

    private Company transCompany(String company) {
        return companyService.findByName(company).orElseThrow(() ->
            new IllegalArgumentException("존재하지 않는 회사입니다.")
        );
    }


}
