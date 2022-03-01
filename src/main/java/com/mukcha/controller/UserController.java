package com.mukcha.controller;

import java.time.LocalDate;
import java.util.Map;

import javax.validation.Valid;

import com.mukcha.domain.Gender;
import com.mukcha.domain.User;
import com.mukcha.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


// ROLE_USER 권한 있어야 진입가능
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 회원 정보 확인 및 수정
    @GetMapping("/edit")
    public String userPage(Model model, @AuthenticationPrincipal User user) {
        log.info(user.getEmail() + " 님의 회원 정보 수정 진입 >>> "+user.toString());
        return "user/userPage";
    }


    @PostMapping(value = "/edit")
    public String updateUserInfo(
            @Valid @ModelAttribute UserForm form, // requestbody = json data
            BindingResult bindingResult,
            Model model,
            Errors errors,
            @AuthenticationPrincipal User user
        ) {

        // 비밀번호 확인 불일치
        if (!form.getPassword().equals(form.getRePassword())) {
            model.addAttribute("form", form); // 실패 시 입력 데이터 유지
            model.addAttribute("valid_rePassword", "비밀번호가 일치하지 않습니다.");
            log.info("valid_rePassword" + "비밀번호가 일치하지 않습니다.");
            return "user/userPage";
        }
        // 닉네임을 변경하였고, 닉네임 중복 시
        if (!user.getNickname().equals(form.getNickname())) {
            if (userService.findByNickname(form.getNickname()).isPresent()) {
                model.addAttribute("form", form);
                model.addAttribute("valid_nickname", "이미 사용중인 닉네임입니다.");
                log.info("valid_nickname"+"이미 사용중인 닉네임입니다.");
                return "user/userPage";
            }
        }
        // @Valid 를 통한 에러 시
        if (errors.hasErrors()) {
            log.info("valid_error");
            model.addAttribute("form", form);
            // 유효성 통과 못한 필드와 메세지 핸들링
            Map<String, String> validatorResult = userService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            return "user/userPage";
        }

        // 통과 시 회원 수정 진행
        userService.updateNickname(user.getUserId(), form.getNickname());
        userService.updatePassword(user.getUserId(), passwordEncoder.encode(form.getPassword()));
        userService.updateGender(user.getUserId(), getGenderForController(form.getGender()));
        userService.updateBirthday(user.getUserId(), getBirthdayForController(form.getBirthYear(), form.getBirthMonth(), form.getBirthDayOfMonth()));
        User savedUser = userService.findUser(user.getUserId()).get();

        // for update principal, re-login
        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser, savedUser.getPassword(), savedUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println(form.getBirthYear()+form.getBirthMonth()+form.getBirthDayOfMonth());
        log.info("회원 정보 수정이 처리되었습니다."+savedUser.toString());
        // model.addAttribute("ifSuccess", "true");

        return "redirect:/";
    }




    // 성별 선택 메소드
    private Gender getGenderForController(String formGender) {
        if (formGender.equals("MALE")) {
            return Gender.MALE;
        } else if (formGender.equals("FEMALE")) {
            return Gender.FEMALE;
        }
        return null;
    }
    
    // 생년월일
    private LocalDate getBirthdayForController(String year, String month, String day) {
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
