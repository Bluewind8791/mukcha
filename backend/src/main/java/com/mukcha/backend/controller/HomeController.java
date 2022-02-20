package com.mukcha.backend.controller;

import com.mukcha.backend.domain.User;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class HomeController {
    
    // root page
    @GetMapping("/")
    public String home() {
        return "home";
    }

    // login page
    @GetMapping("/login")
    public String login() {
        return "user/loginForm";
    }

    // login error - 로그인 에러시 loginForm 으로
    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "loginForm";
    }





}
