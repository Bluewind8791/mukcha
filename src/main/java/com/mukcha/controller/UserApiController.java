package com.mukcha.controller;


import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.mukcha.config.dto.LoginUser;
import com.mukcha.config.dto.SessionUser;
import com.mukcha.controller.dto.SessionUserResponseDto;
import com.mukcha.controller.dto.UserUpdateRequestDto;
import com.mukcha.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;


// ROLE_USER 권한 있어야 진입가능
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserApiController {

    private final UserService userService;


    // 개인정보 업데이트
    @PutMapping(value = "/api/update")
    public String updateUserInfo(
            @Valid @ModelAttribute UserUpdateRequestDto dto, // requestbody = json data
            Model model,
            @LoginUser SessionUser sessionUser,
            RedirectAttributes redirectAttributes
    ) {
        if (sessionUser != null) {
            SessionUserResponseDto user = userService.getSessionUserInfo(sessionUser);
            userService.update(user.getUserId(), dto);
            redirectAttributes.addFlashAttribute("resultMessage", "updateUserSuccess");
        }
        return "redirect:/user/edit";
    }

    // 회원 탈퇴 구현
    @PutMapping(value = "/api/disable")
    public String disableUser(
        Model model,
        @LoginUser SessionUser sessionUser,
        HttpSession httpSession
    ) {
        if (sessionUser != null) {
            SessionUserResponseDto user = userService.getSessionUserInfo(sessionUser);
            userService.disableUser(user.getUserId());
            httpSession.invalidate(); // delete login session
        }
        return "redirect:/";
    }


}