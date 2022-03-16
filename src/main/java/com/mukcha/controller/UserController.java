package com.mukcha.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.mukcha.controller.dto.UserDto;
import com.mukcha.domain.User;
import com.mukcha.service.UserService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
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


    // VIEW - 회원 개인정보 페이지
    @GetMapping("/edit")
    public String viewUserEditPage(Model model, @AuthenticationPrincipal User user) {
        return "user/editForm";
    }


    // 개인정보 업데이트
    @PostMapping(value = "/edit")
    public String updateUserInfo(
            @Valid @ModelAttribute UserDto form, // requestbody = json data
            BindingResult bindingResult,
            Model model,
            Errors errors,
            @AuthenticationPrincipal User user,
            RedirectAttributes redirectAttributes
    ) {
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
        // 통과 시 회원 수정 진행
        userService.updateNickname(user.getUserId(), form.getNickname());
        userService.updatePassword(user.getUserId(), form.getPassword());
        userService.updateGender(user.getUserId(), userService.transClassGender(form.getGender()));
        userService.updateBirthday(user.getUserId(),
            userService.transClassLocalDate(form.getBirthYear(), form.getBirthMonth(), form.getBirthDayOfMonth())
        );
        User savedUser = userService.findUser(user.getUserId()).get();
        // 재로그인
        userService.doLoginWithAuth(savedUser, savedUser.getPassword(), savedUser.getAuthorities());
        // 회원 정보 수정 성공 메세지를 위한 redirect attribute
        redirectAttributes.addFlashAttribute("resultMessage", "success");
        log.info(">>> 회원 <"+savedUser.getEmail()+">님의 개인정보 수정이 처리되었습니다."+savedUser.toString());
        return "redirect:/user/edit";
    }


    // VIEW - 회원 탈퇴 페이지
    @GetMapping(value = "/delete")
    public String viewDisableUserPage() {
        return "user/deleteForm";
    }


    // 회원 탈퇴 구현
    @PostMapping(value = "/delete")
    public String disableUser(
        @ModelAttribute UserDto form,
        Model model,
        @AuthenticationPrincipal User user,
        HttpSession httpSession
    ) {
        // 비밀번호 확인 불일치
        if (!form.getPassword().equals(form.getRePassword())) {
            model.addAttribute("valid_rePassword", "비밀번호가 일치하지 않습니다.");
            log.info("valid_rePassword" + "위의 비밀번호와 동일하지 않습니다.");
            return "user/deleteForm";
        }
        // 현재 비밀번호와 불일치
        if (!userService.isPasswordSame(form.getPassword(), user.getPassword())) {
            model.addAttribute("valid_password", "비밀번호가 일치하지 않습니다.");
            log.info("valid_password" + "비밀번호가 일치하지 않습니다.");
            return "user/deleteForm";
        }
        httpSession.invalidate(); // delete login session
        String disabledUser = user.getEmail();
        userService.disableUser(user.getUserId()); // disable user
        log.info(">>> 회원 <"+disabledUser+">님이 탈퇴 처리되었습니다.");
        return "redirect:/";
    }


}
