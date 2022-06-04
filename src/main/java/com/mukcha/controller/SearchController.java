package com.mukcha.controller;


import com.mukcha.config.dto.LoginUser;
import com.mukcha.config.dto.SessionUser;
import com.mukcha.domain.Search;
import com.mukcha.service.CompanyService;
import com.mukcha.service.FoodService;
import com.mukcha.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final UserService userService;
    private final FoodService foodService;
    private final CompanyService companyService;


    @GetMapping("/search")
    public String search(
        @RequestParam(required = false) String keyword,
        Model model,
        @LoginUser SessionUser sessionUser
    ) {
        if (sessionUser != null) {
            model.addAttribute("loginUser", userService.getSessionUserInfo(sessionUser));
        }
        model.addAttribute("foodList", foodService.findAll(Search.foodSearching(keyword)));
        model.addAttribute("companyList", companyService.findAll(Search.companySearching(keyword)));
        model.addAttribute("userList", userService.findAll(Search.userSearching(keyword)));
        model.addAttribute("searchWord", keyword);
        return "search/search";
    }

    @GetMapping("/search/menu")
    public String foodSearchList(
        @RequestParam(required = false) String keyword,
        Model model,
        @LoginUser SessionUser sessionUser
    ) {
        if (sessionUser != null) {
            model.addAttribute("loginUser", userService.getSessionUserInfo(sessionUser));
        }
        model.addAttribute("foodList", foodService.findAll(Search.foodSearching(keyword)));
        model.addAttribute("searchWord", keyword);
        return "search/list";
    }

    @GetMapping("/search/company")
    public String companySearchList(
        @RequestParam(required = false) String keyword,
        Model model,
        @LoginUser SessionUser sessionUser
    ) {
        if (sessionUser != null) {
            model.addAttribute("loginUser", userService.getSessionUserInfo(sessionUser));
        }
        model.addAttribute("companyList", companyService.findAll(Search.companySearching(keyword)));
        model.addAttribute("searchWord", keyword);
        return "search/list";
    }


}
