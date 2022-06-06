package com.mukcha.controller.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.mukcha.config.dto.LoginUser;
import com.mukcha.config.dto.SessionUser;
import com.mukcha.config.dto.SpringDocApiResponse;
import com.mukcha.controller.dto.UserUpdateRequestDto;
import com.mukcha.domain.ErrorMessage;
import com.mukcha.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.hateoas.mediatype.problem.Problem;
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
@Tag(name = "User API Controller",
    description = "ROLE_USER의 권한을 가진 사용자가 접근 가능한 회원 정보 API입니다.")
public class UserApiController {

    private final UserService userService;


    @SpringDocApiResponse
    @PutMapping("/{userId}")
    @Operation(summary = "회원 정보 수정 메소드", description = "회원의 개인 정보를 수정합니다.")
    public ResponseEntity<?> update(
            @Parameter(description = "수정하고자 하는 회원의 ID", required = true) @PathVariable Long userId,
            @RequestBody UserUpdateRequestDto dto, // json data
            @Parameter(hidden = true) @LoginUser SessionUser sessionUser
    ) {
        if (userService.update(userId, dto)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body(Problem.create()
            .withTitle(ErrorMessage.FAIL_UPDATE.name())
            .withDetail(ErrorMessage.FAIL_UPDATE.getMessage())
        );
    }

    @SpringDocApiResponse
    @PatchMapping("/{userId}")
    @Operation(summary = "회원 탈퇴 메소드", description = "회원 탈퇴를 진행합니다. (enable=false)")
    public ResponseEntity<?> disableUser(
        @Parameter(description = "수정하고자 하는 회원의 ID", required = true) @PathVariable Long userId,
        @Parameter(hidden = true) @LoginUser SessionUser sessionUser,
        HttpServletRequest request
    ) {
        boolean result = userService.disableUser(userId);
        HttpSession session = request.getSession();
        if (result) {
            session.invalidate(); // delete login session
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body(Problem.create()
                .withTitle(ErrorMessage.FAIL_UPDATE.name())
                .withDetail(ErrorMessage.FAIL_UPDATE.getMessage())
            );
        }
    }


}