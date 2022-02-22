package com.mukcha.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class HomeController {
    
    // root page
    @GetMapping("/")
    public String home() {
        return "home";
    }

    // login page
    @GetMapping("/login")
    public String login(
            @RequestParam(value = "error", defaultValue = "false") Boolean error,
            Model model) {
        model.addAttribute("error", error);
        return "user/loginForm";
    }





}
