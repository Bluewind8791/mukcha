package com.mukcha.controller;

import com.mukcha.service.FoodService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class HomeController {

    private final FoodService foodService;

    // root page
    @GetMapping(value = {"/", ""})
    public String home(Model model) {
        model.addAttribute("avgScoreTopTen", foodService.findTopTenOrderByScore());
        model.addAttribute("newestTopTen", foodService.findTopTenNewest());
        return "home";
    }

    // login page
    @GetMapping("/login")
    public String login(
            @RequestParam(value = "error", defaultValue = "false") Boolean error,
            Model model
        ) {
        model.addAttribute("error", error);
        return "user/loginForm";
    }

}
