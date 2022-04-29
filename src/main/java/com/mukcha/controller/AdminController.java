package com.mukcha.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mukcha.config.dto.LoginUser;
import com.mukcha.config.dto.SessionUser;
import com.mukcha.controller.dto.CompanyRequestDto;
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

    /* >>> VIEW <<< */

    // ROOT PAGE
    @GetMapping(value = {"/", ""})
    public String adminHome(Model model, @LoginUser SessionUser sessionUser) {
        if (sessionUser != null) {
            UserDto user = userService.getSessionUserInfo(sessionUser);
            model.addAttribute("login_user_id", user.getUserId());
            model.addAttribute("login_user_nickname", user.getNickname());
        }
        log.info(">>> 관리자 페이지에 진입하였습니다.");
        // 회사 추가하기
        // 크롤링 시작
        // 모든 회사 리스트
        model.addAttribute("companyList", companyService.findAll());
        // 최근 메뉴 리스트 10개
        model.addAttribute("foodList", foodService.findFoodTopTenNewest());
        // Category List
        List<Category> categoryList = List.of(Category.values());
        model.addAttribute("categoryList", categoryList);
        return "admin/adminHome";
    }


    // 해당 회사의 모든 메뉴 리스트 
    // /admin/company/'+${c.companyId}
    @GetMapping(value = "/company/{companyId}")
    public String viewAllCompanies(
        Model model,
        @LoginUser SessionUser sessionUser,
        @PathVariable Long companyId
    ) {
        if (sessionUser != null) {
            UserDto user = userService.getSessionUserInfo(sessionUser);
            model.addAttribute("login_user_id", user.getUserId());
            model.addAttribute("login_user_nickname", user.getNickname());
        }
        model.addAttribute("foodList", companyService.getFoodList(companyId));
        // 모든 회사 이름만
        model.addAttribute("companyList", companyService.findAllCompanyName());
        // Category List
        List<Category> categoryList = List.of(Category.values());
        model.addAttribute("categoryList", categoryList);
        return "admin/adminCompany";
    }


    // 모든 메뉴 리스트
    @GetMapping(value = "/menus")
    public String viewAllMenus(Model model, @LoginUser SessionUser sessionUser) {
        if (sessionUser != null) {
            UserDto user = userService.getSessionUserInfo(sessionUser);
            model.addAttribute("login_user_id", user.getUserId());
            model.addAttribute("login_user_nickname", user.getNickname());
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
        List<CompanyRequestDto> companyDtoList = new ArrayList<>();
        companies.stream().forEach(com -> {
            CompanyRequestDto companyDto = new CompanyRequestDto();
            companyDto.setCompanyName(com.getName());
            companyDtoList.add(companyDto);
        });
        model.addAttribute("companyList", companyDtoList);
        return "admin/adminMenuList";
    }



    //>>> METHODS <<<

    // 회사 추가 메소드
    @PostMapping(value = "/company")
    public String addCompany(
            @ModelAttribute CompanyRequestDto companyDto,
            Model model,
            HttpServletRequest request
    ) {
        Company company = Company.builder()
                            .name(companyDto.getCompanyName())
                            .image(companyDto.getCompanyLogo())
                            .build()
        ;
        Company savedCompany = companyService.save(company);
        log.info("회사가 생성되었습니다." + savedCompany.toString());
        String referer = request.getHeader("Referer"); // 헤더에서 이전 페이지를 읽는다.
        return "redirect:"+ referer; // 이전 페이지로 리다이렉트
    }


    // 메뉴(음식) 추가 메소드
    @PostMapping(value = "/menu")
    public String addMenu(
            @ModelAttribute FoodDto foodDto,
            Model model,
            HttpServletRequest request
    ) {
        Food food = Food.builder()
                        .name(foodDto.getFoodName())
                        .image(foodDto.getFoodImage())
                        .category(transCategory(foodDto.getCategory()))
                        .company(companyService.findByName(foodDto.getCompanyName()))
                        .build()
        ;
        Food savedFood = foodService.save(food);
        log.info("메뉴가 생성되었습니다." + savedFood.toString());
        String referer = request.getHeader("Referer"); // 헤더에서 이전 페이지를 읽는다.
        return "redirect:"+ referer; // 이전 페이지로 리다이렉트
    }


    // 회사 삭제 메소드
    @DeleteMapping(value = "/companies/delete/{companyId}")
    public String deleteCompany(@PathVariable Long companyId) {
        companyService.deleteCompany(companyId);
        return "redirect:/admin";
    }


    // 메뉴 삭제 메소드
    @DeleteMapping(value = "/menus/delete/{foodId}")
    public String deleteFood(@PathVariable Long foodId) {
        foodService.deleteFood(foodId);
        return "redirect:/admin/menus/";
    }


    // 회사 수정 메소드
    @PostMapping(value = "/companies/edit/{companyId}")
    public String editCompany(
        @PathVariable Long companyId,
        CompanyRequestDto companyDto
    ) {
        companyService.editCompanyName(companyId, companyDto.getCompanyName());
        companyService.editCompanyLogo(companyId, companyDto.getCompanyLogo());
        return "redirect:/admin/companies/";
    }


    // 메뉴 수정 메소드
    @PostMapping(value = "/menus/edit/{foodId}")
    public String editFood(
        @PathVariable Long foodId,
        FoodDto foodDto,
        HttpServletRequest request
    ) {
        System.out.println(">>> company name:"+foodDto.getCompanyName());
        // foodService.editFoodName(foodId, foodDto.getFoodName());
        // foodService.editFoodImage(foodId, foodDto.getFoodImage());
        foodService.editFoodCompany(foodId, foodDto.getCompanyName());
        // foodService.editFoodCategory(foodId, transCategory(foodDto.getCategory()));
		String referer = request.getHeader("Referer"); // 헤더에서 이전 페이지를 읽는다.
        return "redirect:"+ referer; // 이전 페이지로 리다이렉트
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


}
