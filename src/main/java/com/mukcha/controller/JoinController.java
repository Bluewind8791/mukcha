package com.mukcha.controller;

import java.time.LocalDate;
import java.util.Map;
import javax.validation.Valid;

import com.mukcha.domain.Authority;
import com.mukcha.domain.Gender;
import com.mukcha.domain.User;
import com.mukcha.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequiredArgsConstructor
public class JoinController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    // 회원가입 - GET
    @GetMapping("/join")
    public String joinForm(Model model, UserForm form) {
        model.addAttribute("form", new UserForm());
        return "user/joinForm";
    }

    // 회원가입 - POST
    @PostMapping("/join")
    public String join(@Valid UserForm form, BindingResult bindingResult, Model model, Errors errors) {

        // 비밀번호 확인 불일치
        if (!form.getPassword().equals(form.getRePassword())) {
            model.addAttribute("form", form); // 회원가입 실패 시 입력 데이터 유지
            model.addAttribute("valid_rePassword", "비밀번호가 일치하지 않습니다.");
            return "user/joinForm";
        }
        // 이메일 중복 시
        if (userService.findByEmail(form.getEmail()).isPresent()) {
            model.addAttribute("form", form);
            model.addAttribute("valid_email", "이미 사용중인 이메일입니다.");
            return "user/joinForm";
        }
        // 닉네임 중복 시
        if (userService.findByNickname(form.getNickname()).isPresent()) {
            model.addAttribute("form", form);
            model.addAttribute("valid_nickname", "이미 사용중인 닉네임입니다.");
            return "user/joinForm";
        }
        // @Valid 를 통한 에러 시
        if (errors.hasErrors()) {
            model.addAttribute("form", form);
            // 유효성 통과 못한 필드와 메세지 핸들링
            Map<String, String> validatorResult = userService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            return "user/joinForm";
        }

        // 통과 시 회원가입 진행
        final User user = User.builder()
                            .email(form.getEmail())
                            .nickname(form.getNickname())
                            .password(passwordEncoder.encode(form.getPassword()))
                            .gender(getGenderForJoin(form.getGender()))
                            .birthday(getBirthday(form.getBirthYear(), form.getBirthMonth(), form.getBirthDayOfMonth()))
                            .enabled(true)
                            .build();
        User savedUser = userService.save(user);
        userService.addAuthority(savedUser.getUserId(), Authority.ROLE_USER);
        log.info("회원가입이 처리되었습니다." + user.toString());
        return "redirect:/login";
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
