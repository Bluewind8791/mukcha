package com.mukcha.service;

import java.util.Optional;

import com.mukcha.config.dto.SessionUser;
import com.mukcha.controller.dto.SessionUserResponseDto;
import com.mukcha.controller.dto.UserUpdateRequestDto;
import com.mukcha.controller.dto.UserResponseDto;
import com.mukcha.domain.ErrorMessage;
import com.mukcha.domain.Gender;
import com.mukcha.domain.User;
import com.mukcha.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    // 회원가입 서비스
    public User signUp(User user) {
        // 프로필 이미지가 없을 경우 기본 프로필로 대체
        if (user.getProfileImage() == null) {
            user.editProfileImage("/profile/blank.png");
        }
        // set enable
        user.enableUser();
        log.info("회원가입이 진행되었습니다." + user.toString());
        return userRepository.save(user);
    }

    // 회원 정보 업데이트
    public void update(Long userId, UserUpdateRequestDto dto) {
        findUser(userId).update(dto.getNickname(), dto.getProfileImage(), dto.getGender(), dto.getBirthYear());
        log.info(">>> 개인정보 수정이 처리되었습니다."+ userId);
    }

    // 회원 탈퇴
    @Transactional(rollbackFor = {RuntimeException.class})
    public void disableUser(Long userId) {
        User user = findUser(userId);
        user.disableUser(); // disable 처리
        userRepository.save(user);
    }


    /* FINDING SERVICES */
    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> 
            new IllegalArgumentException(ErrorMessage.USER_NOT_FOUND.getMessage())
        );
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> 
            new IllegalArgumentException(ErrorMessage.USER_NOT_FOUND.getMessage())
        );
    }

    public Optional<User> findByEmailOr(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByNickname(String nickname) {
        return userRepository.findByNickname(nickname).orElseThrow(() -> 
            new IllegalArgumentException(ErrorMessage.USER_NOT_FOUND.getMessage())
        );
    }

    public UserResponseDto findByUserIdIntoDto(Long userId) {
        return new UserResponseDto(findUser(userId));
    }




    // 로그인된 회원의 정보를 불러온다.
    public SessionUserResponseDto getSessionUserInfo(SessionUser sessionUser) {
        return new SessionUserResponseDto(findByEmail(sessionUser.getEmail()));
    }

    // 성별 클래스 전환 for DTO
    public Gender transClassGender(String stringGender) {
        if (stringGender.equals("MALE")) {
            return Gender.MALE;
        } else if (stringGender.equals("FEMALE")) {
            return Gender.FEMALE;
        }
        return null;
    }


}