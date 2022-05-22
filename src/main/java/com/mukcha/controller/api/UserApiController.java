package com.mukcha.controller.api;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.mukcha.config.dto.LoginUser;
import com.mukcha.config.dto.SessionUser;
import com.mukcha.controller.dto.UserUpdateRequestDto;
import com.mukcha.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserInfo(
            @PathVariable Long userId,
            @RequestBody UserUpdateRequestDto dto, // json data
            @LoginUser SessionUser sessionUser
    ) {
        if (sessionUser.getEmail() == userService.findUser(userId).getEmail()) {
            userService.update(userId, dto);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // 회원 탈퇴 구현
    @PatchMapping("/{userId}")
    public ResponseEntity<?> disableUser(
        @PathVariable Long userId,
        @LoginUser SessionUser sessionUser,
        HttpServletRequest request
    ) {
        if (sessionUser.getEmail() == userService.findUser(userId).getEmail()) {
            userService.disableUser(userId);
            HttpSession session = request.getSession();
            session.invalidate(); // delete login session
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}