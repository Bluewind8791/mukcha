package com.mukcha.controller;


import com.mukcha.domain.Search;
import com.mukcha.repository.CompanyRepository;
import com.mukcha.repository.FoodRepository;
import com.mukcha.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    @Autowired private FoodRepository foodRepository;
    @Autowired private CompanyRepository companyRepository;
    @Autowired private UserRepository userRepository;


    @GetMapping(value = "/search")
    public String search(
        @RequestParam(required = false) String keyword,
        Model model
    ) {
        model.addAttribute("foodSearchList", 
            foodRepository.findAll(Search.foodSearching(keyword))
        );
        model.addAttribute("companySearchList", 
            companyRepository.findAll(Search.companySearching(keyword))
        );
        model.addAttribute("userSearchList", 
            userRepository.findAll(Search.userSearching(keyword))
        );
        model.addAttribute("searchWord", keyword);
        return "/search";
    }


}
