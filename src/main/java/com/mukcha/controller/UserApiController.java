package com.mukcha.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.mukcha.config.dto.LoginUser;
import com.mukcha.config.dto.SessionUser;
import com.mukcha.controller.dto.SessionUserResponseDto;
import com.mukcha.controller.dto.UserUpdateRequestDto;
import com.mukcha.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;


// ROLE_USER 권한 있어야 진입가능
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/users")
public class UserApiController {

    private final UserService userService;


    // 회원 정보 수정
    @PutMapping
    public ResponseEntity<?> updateUserInfo(
            @RequestBody UserUpdateRequestDto dto, // json data
            @LoginUser SessionUser sessionUser
    ) {
        if (sessionUser != null) {
            SessionUserResponseDto sessionDto = userService.getSessionUserInfo(sessionUser);
            Long userId = sessionDto.getUserId();
            userService.update(userId, dto);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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