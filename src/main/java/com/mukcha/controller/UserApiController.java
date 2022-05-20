package com.mukcha.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.mukcha.config.dto.LoginUser;
import com.mukcha.config.dto.SessionUser;
import com.mukcha.controller.dto.SessionUserResponseDto;
import com.mukcha.controller.dto.UserUpdateRequestDto;
import com.mukcha.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;


// ROLE_USER 권한 있어야 진입가능
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/users")
public class UserApiController {

    private final UserService userService;


    @PutMapping(value = "/{userId}")
    public ModelAndView updateUserInfo(
            @PathVariable Long userId,
            @Valid @ModelAttribute UserUpdateRequestDto dto, // json data
            @LoginUser SessionUser sessionUser
    ) {
        ModelAndView mv = new ModelAndView("user/editForm");
        if (sessionUser != null) {
            SessionUserResponseDto sessionDto = userService.getSessionUserInfo(sessionUser);
            if (sessionDto.getUserId() == userId) {
                userService.update(userId, dto);
                mv.setStatus(HttpStatus.OK);
                mv.addObject("loginUser", sessionDto);
                mv.addObject("user", userService.findByUserId(userId));
                mv.addObject("resultMessage", "updateUserSuccess");
                return mv;
            }
        }
        mv.setStatus(HttpStatus.BAD_REQUEST);
        mv.addObject("resultMessage", "updateUserFail");
        return mv;
    }


    // 회원 탈퇴 구현
    @PatchMapping
    public ResponseEntity<?> disableUser(
        @LoginUser SessionUser sessionUser,
        HttpServletRequest request
    ) {
        if (sessionUser != null) {
            SessionUserResponseDto userDto = userService.getSessionUserInfo(sessionUser);
            if (userDto.getUserId() == userDto.getUserId()) {
                userService.disableUser(userDto.getUserId());
                HttpSession session = request.getSession();
                session.invalidate(); // delete login session
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}