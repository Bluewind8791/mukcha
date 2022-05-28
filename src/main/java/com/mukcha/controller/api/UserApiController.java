package com.mukcha.controller.api;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.mukcha.config.dto.LoginUser;
import com.mukcha.config.dto.SessionUser;
import com.mukcha.controller.dto.UserUpdateRequestDto;
import com.mukcha.service.UserService;

import org.springframework.hateoas.EntityModel;
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
public class UserApiController {

    private final UserService userService;


    // 회원 정보 수정
    @PutMapping("/{userId}")
    public ResponseEntity<?> update(
            @PathVariable Long userId,
            @RequestBody UserUpdateRequestDto dto, // json data
            @LoginUser SessionUser sessionUser
    ) {
        return ResponseEntity.ok(EntityModel.of(userService.update(userId, dto))
            .add(linkTo(methodOn(UserApiController.class).update(userId, dto, sessionUser)).withSelfRel()
        ));
    }

    // 회원 탈퇴 구현
    @PatchMapping("/{userId}")
    public ResponseEntity<?> disableUser(
        @PathVariable Long userId,
        @LoginUser SessionUser sessionUser,
        HttpServletRequest request
    ) {
        boolean result = userService.disableUser(userId);
        HttpSession session = request.getSession();
        if (result) {
            session.invalidate(); // delete login session
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body(Problem.create()
                .withTitle("회원 탈퇴에 실패하였습니다.")
                .withDetail("다시 시도해주세요.")
            );
        }
    }


}