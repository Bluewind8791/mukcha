package com.mukcha.controller;


import java.time.LocalDate;

import com.mukcha.domain.Authority;
import com.mukcha.domain.Gender;
import com.mukcha.domain.User;
import com.mukcha.service.UserService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


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


    // 회원가입 - GET
    @GetMapping("/join")
    public String joinForm() {
        return "user/joinForm";
    }

    // 회원가입 - POST
    @PostMapping("/join")
    public String join(UserForm form) {

        final User user = User.builder()
            .email(form.getEmail())
            .nickname(form.getNickname())
            .password(passwordEncoder.encode(form.getPassword()))
            .gender(getGenderForJoin(form.getGender()))
            .birthday(getBirthday(form.getBirthYear(), form.getBirthMonth(), form.getBirthDay()))
            .enabled(true)
            .build();

        User savedUser = userService.join(user);
        userService.addAuthority(savedUser.getUserId(), Authority.ROLE_USER);
        
        log.info("회원가입이 처리되었습니다." + user.toString());
        return "redirect:/";
    }

    // 회원가입 시 성별 선택 메소드
    private Gender getGenderForJoin(String formGender) {
        if (formGender.equals("MALE")) {
            return Gender.MALE;
        } else if (formGender.equals("FEMALE")) {
            return Gender.FEMALE;
        }
        return null;
    }

    // 회원가입 시 생년월일
    private LocalDate getBirthday(String year, String month, String day) {
        if (year == null || month == null || day == null) {
            return null;
        } else if (year.isEmpty() || month.isEmpty() || day.isEmpty()) {
            return null;
        }
        Integer intYear = Integer.parseInt(year);
        Integer intMonth = Integer.parseInt(month);
        Integer intDay = Integer.parseInt(day);
        return LocalDate.of(intYear, intMonth, intDay);
    }




}
