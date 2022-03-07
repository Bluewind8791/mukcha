package com.mukcha.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    @GetMapping(value = {"/", ""})
    public String adminHome(Model model) {
        log.info("관리자 페이지에 진입하였습니다.");

        List<Category> categoryList = List.of(Category.values());
        model.addAttribute("categoryList", categoryList);

        model.addAttribute("companyList", companyService.findAll());
        model.addAttribute("foodList", foodService.findAll());

        return "admin/adminHome";
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
                            .foods(null)
                            .build()
        ;
        Company savedCompany = companyService.save(company);
        log.info("회사가 생성되었습니다." + savedCompany.toString());
        return "redirect:/admin/";
    }


    // 메뉴 추가하기
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
                        .company(transCompany(foodDto.getCompany()))
                        .build()
        ;
        Food savedFood = foodService.save(food);
        log.info("메뉴가 생성되었습니다." + savedFood.toString());
        return "redirect:/admin/";
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
