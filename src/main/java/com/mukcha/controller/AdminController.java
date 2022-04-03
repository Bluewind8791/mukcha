package com.mukcha.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mukcha.config.dto.LoginUser;
import com.mukcha.config.dto.SessionUser;
import com.mukcha.controller.dto.CompanyDto;
import com.mukcha.controller.dto.FoodDto;
import com.mukcha.controller.dto.UserDto;
import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.service.CompanyService;
import com.mukcha.service.FoodService;
import com.mukcha.service.UserService;

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


// ADMIN 권한 있어야 진입가능
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/admin")
public class AdminController {

    private final CompanyService companyService;
    private final FoodService foodService;
    private final UserService userService;


    // VIEW - ROOT PAGE
    @GetMapping(value = {"/", ""})
    public String adminHome(Model model, @LoginUser SessionUser sessionUser) {
        if (sessionUser != null) {
            UserDto user = userService.getSessionUserInfo(sessionUser);
            model.addAttribute("userId", user.getUserId());
            model.addAttribute("nickname", user.getNickname());
        }
        log.info(">>> 관리자 페이지에 진입하였습니다.");
        model.addAttribute("foodList", foodService.findFoodTopTenNewest());
        model.addAttribute("companyList", companyService.findCompanyTopTenNewest());
        // Category List
        List<Category> categoryList = List.of(Category.values());
        model.addAttribute("categoryList", categoryList);
        return "admin/adminHome";
    }


    // VIEW - 모든 메뉴 리스트
    @GetMapping(value = "/menus")
    public String viewAllMenus(Model model, @LoginUser SessionUser sessionUser) {
        if (sessionUser != null) {
            UserDto user = userService.getSessionUserInfo(sessionUser);
            model.addAttribute("userId", user.getUserId());
            model.addAttribute("nickname", user.getNickname());
        }
        // 카테고리 리스트
        List<Category> categoryList = List.of(Category.values());
        model.addAttribute("categoryList", categoryList);
        // 메뉴 정보 -> DTO 변경
        // foodName, foodId, foodImage, foodCategory, foodCompanyName
        List<Food> foodList = foodService.findAll();
        Collections.reverse(foodList); // 최신순 정렬
        List<FoodDto> foodDtoList = new ArrayList<>();
        foodList.stream().forEach(food -> {
            FoodDto foodDto = new FoodDto();
            foodDto.setFoodId(food.getFoodId());
            foodDto.setFoodName(food.getName());
            foodDto.setFoodImage(food.getImage());
            foodDto.setCategory(food.getCategory().name());
            foodDto.setCompanyName(food.getCompany().getName());
            foodDtoList.add(foodDto);
        });
        model.addAttribute("foodList", foodDtoList);
        // 모든 회사 이름 -> DTO 변경
        List<Company> companies = companyService.findAll();
        List<CompanyDto> companyDtoList = new ArrayList<>();
        companies.stream().forEach(com -> {
            CompanyDto companyDto = new CompanyDto();
            companyDto.setCompanyName(com.getName());
            companyDtoList.add(companyDto);
        });
        model.addAttribute("companyList", companyDtoList);
        return "admin/adminMenuList";
    }


    // VIEW - 모든 회사 리스트
    @GetMapping(value = "/companies")
    public String viewAllCompanies(Model model, @LoginUser SessionUser sessionUser) {
        if (sessionUser != null) {
            UserDto user = userService.getSessionUserInfo(sessionUser);
            model.addAttribute("userId", user.getUserId());
            model.addAttribute("nickname", user.getNickname());
        }
        model.addAttribute("companyList", companyService.findAll());
        return "admin/adminCompanyList";
    }


    //>>> METHODS <<<

    // 회사 추가 메소드
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


    // 메뉴(음식) 추가 메소드
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


    // 회사 삭제 메소드
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


    // 메뉴 삭제 메소드
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


    // 회사 수정 메소드
    @PostMapping(value = "/companies/edit/{companyId}")
    public String editCompany(
        @PathVariable Long companyId,
        CompanyDto companyDto
    ) {
        companyService.editCompanyName(companyId, companyDto.getCompanyName());
        companyService.editCompanyLogo(companyId, companyDto.getCompanyLogo());
        return "redirect:/admin/companies/";
    }


    // 메뉴 수정 메소드
    @PostMapping(value = "/menus/edit/{foodId}")
    public String editFood(
        @PathVariable Long foodId,
        FoodDto foodDto
    ) {
        System.out.println(">>> company name:"+foodDto.getCompanyName());
        foodService.editFoodName(foodId, foodDto.getFoodName());
        foodService.editFoodImage(foodId, foodDto.getFoodImage());
        foodService.editFoodCompany(foodId, foodDto.getCompanyName());
        foodService.editFoodCategory(foodId, transCategory(foodDto.getCategory()));
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
