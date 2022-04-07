package com.mukcha.controller;


import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.mukcha.config.dto.LoginUser;
import com.mukcha.config.dto.SessionUser;
import com.mukcha.controller.dto.UserDto;
import com.mukcha.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


// ROLE_USER 권한 있어야 진입가능
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;


    // >>> VIEW <<<
    // 회원 개인정보 수정 페이지
    @GetMapping("/edit")
    public String viewUserEditPage(Model model, @LoginUser SessionUser sessionUser) {
        // Login User
        if (sessionUser != null) {
            UserDto user = userService.getSessionUserInfo(sessionUser);
            model.addAttribute("login_user_id", user.getUserId());
            model.addAttribute("login_user_nickname", user.getNickname());
            model.addAttribute("user", user);
        }
        return "user/editForm";
    }


    // 회원 탈퇴 페이지
    @GetMapping(value = "/delete")
    public String viewDisableUserPage(@LoginUser SessionUser sessionUser, Model model) {
        // Login User
        if (sessionUser != null) {
            UserDto user = userService.getSessionUserInfo(sessionUser);
            model.addAttribute("login_user_id", user.getUserId());
            model.addAttribute("login_user_nickname", user.getNickname());
        }
        return "user/deleteForm";
    }


    // >>> METHODS <<<
    // 회원 탈퇴 구현
    @PostMapping(value = "/delete")
    public String disableUser(
        @ModelAttribute UserDto form,
        Model model,
        @LoginUser SessionUser sessionUser,
        HttpSession httpSession
    ) {
        if (sessionUser != null) {
            UserDto user = userService.getSessionUserInfo(sessionUser);
            httpSession.invalidate(); // delete login session
            String disabledUser = user.getEmail();
            userService.disableUser(user.getUserId()); // disable user
            log.info(">>> 회원 <"+disabledUser+">님이 탈퇴 처리되었습니다.");
        }
        return "redirect:/";
    }


    // 개인정보 업데이트
    @PostMapping(value = "/edit")
    public String updateUserInfo(
            @Valid @ModelAttribute UserDto form, // requestbody = json data
            BindingResult bindingResult,
            Model model,
            Errors errors,
            @LoginUser SessionUser sessionUser,
            RedirectAttributes redirectAttributes
    ) {
        if (sessionUser != null) {
            UserDto user = userService.getSessionUserInfo(sessionUser);
            userService.updateGender(user.getUserId(), form.getGender());
            userService.updateBirthYear(user.getUserId(), form.getBirthYear());
            redirectAttributes.addFlashAttribute("resultMessage", "success");
            log.info(">>> 회원 <"+user.getEmail()+">님의 개인정보 수정이 처리되었습니다."+user.toString());
        }
        return "redirect:/user/edit";
    }


}
/*
userService.updateNickname(user.getUserId(), form.getNickname());
userService.updatePassword(user.getUserId(), form.getPassword());
// 비밀번호 확인 불일치
if (!form.getPassword().equals(form.getRePassword())) {
    model.addAttribute("form", form); // 실패 시 입력 데이터 유지
    model.addAttribute("valid_rePassword", "비밀번호가 일치하지 않습니다.");
    log.info("valid_rePassword" + "비밀번호가 일치하지 않습니다.");
    return "user/editForm";
}
// 닉네임을 변경하였고, 닉네임 중복 시
if (!user.getNickname().equals(form.getNickname())) {
    if (userService.findByNickname(form.getNickname()).isPresent()) {
        model.addAttribute("form", form);
        model.addAttribute("valid_nickname", "이미 사용중인 닉네임입니다.");
        log.info("valid_nickname"+"이미 사용중인 닉네임입니다.");
        return "user/editForm";
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
    return "user/editForm";
}
*/