package com.mukcha.controller;


import com.mukcha.config.dto.LoginUser;
import com.mukcha.config.dto.SessionUser;
import com.mukcha.domain.Search;
import com.mukcha.repository.CompanyRepository;
import com.mukcha.repository.FoodRepository;
import com.mukcha.repository.UserRepository;
import com.mukcha.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final FoodRepository foodRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final UserService userService;


    @GetMapping(value = "/search")
    public String search(
        @RequestParam(required = false) String keyword,
        Model model,
        @LoginUser SessionUser sessionUser
    ) {
        if (sessionUser != null) {
            model.addAttribute("loginUser", userService.getSessionUserInfo(sessionUser));
        }
        model.addAttribute("foodList", 
            foodRepository.findAll(Search.foodSearching(keyword))
        );
        model.addAttribute("companyList", 
            companyRepository.findAll(Search.companySearching(keyword))
        );
        model.addAttribute("userList", 
            userRepository.findAll(Search.userSearching(keyword))
        );
        model.addAttribute("searchWord", keyword);
        return "search/search";
    }

    @GetMapping(value = "/search/menu")
    public String foodSearchList(
        @RequestParam(required = false) String keyword,
        Model model,
        @LoginUser SessionUser sessionUser
    ) {
        if (sessionUser != null) {
            model.addAttribute("loginUser", userService.getSessionUserInfo(sessionUser));
        }
        model.addAttribute("foodList", 
            foodRepository.findAll(Search.foodSearching(keyword))
        );
        model.addAttribute("searchWord", keyword);
        return "search/list";
    }

    @GetMapping(value = "/search/company")
    public String companySearchList(
        @RequestParam(required = false) String keyword,
        Model model,
        @LoginUser SessionUser sessionUser
    ) {
        if (sessionUser != null) {
            model.addAttribute("loginUser", userService.getSessionUserInfo(sessionUser));
        }
        model.addAttribute("companyList", 
            companyRepository.findAll(Search.companySearching(keyword))
        );
        model.addAttribute("searchWord", keyword);
        return "search/list";
    }


}
