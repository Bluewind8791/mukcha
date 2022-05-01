package com.mukcha.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.mukcha.config.dto.SessionUser;
import com.mukcha.controller.dto.SessionUserResponseDto;
import com.mukcha.controller.dto.UserDto;
import com.mukcha.controller.dto.UserResponseDto;
import com.mukcha.domain.ErrorMessage;
import com.mukcha.domain.Gender;
import com.mukcha.domain.User;
import com.mukcha.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

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

    // 회원가입 시 유효성 체크
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();
        // 유효성 검사에 실패한 필드들은 Map 자료구조를 이용하여 키값과 에러 메시지를 응답
        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField()); // 키 : valid_{dto 필드명}
            validatorResult.put(validKeyName, error.getDefaultMessage()); // 메시지 : dto에서 작성한 message 값
        }
        return validatorResult;
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



    /* UPDATE SERVICES */
    public void update(Long userId, UserDto dto) {
        // log.info(">>> 회원 <"+user.getEmail()+">님의 개인정보 수정이 처리되었습니다."+user.toString());
    }

    public void updateEmail(Long userId, String email) {
        User user = findUser(userId);
        user.editEmail(email);
        userRepository.save(user);
    }

    public void updateNickname(Long userId, String nickname) {
        User user = findUser(userId);
        user.editNickname(nickname);
        userRepository.save(user);
    }

    public void updateProfileImage(Long userId, String profileImageUrl) {
        User user = findUser(userId);
        user.editProfileImage(profileImageUrl);
        userRepository.save(user);
    }

    public void updateGender(Long userId, String stringGender) {
        User user = findUser(userId);
        Gender gender = transClassGender(stringGender);
        user.editGender(gender);
        userRepository.save(user);
    }

    public void updateBirthYear(Long userId, String birthYear) {
        User user = findUser(userId);
        user.editBirthyear(birthYear);
        userRepository.save(user);
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

    // 회원 삭제 (enabled = false)
    @Transactional(rollbackFor = {RuntimeException.class})
    public void disableUser(Long userId) {
        User user = findUser(userId);
        user.editNickname(null); // nickname null 처리
        user.disableUser(); // disable 처리
        userRepository.save(user);
    }


}
/*

    private final AuthenticationManager authenticationManager;
    // 권한 부여
    public void addAuthority(Long userId, String authority) {
        userRepository.findById(userId).ifPresent(user -> {
            Authority newRole = new Authority(user.getUserId(), authority);
            if (user.getAuthorities() == null) { // 아무 권한이 없다면
                HashSet<Authority> authorities = new HashSet<>();
                authorities.add(newRole);
                user.setAuthorities(authorities);
                userRepository.save(user);
            } else if (!user.getAuthorities().contains(newRole)) { // 해당 권한만 가지고 있지 않다면
                HashSet<Authority> authorities = new HashSet<>();
                authorities.addAll(user.getAuthorities()); // 기존의 권한에서
                authorities.add(newRole); // new role 추가
                user.setAuthorities(authorities);
                userRepository.save(user);
            }
        });
    }
    // 권한 제거
    public void removeAuthority(Long userId, String authority) {
        userRepository.findById(userId).ifPresent(user -> {
            Authority targetRole = new Authority(user.getUserId(), authority); // target role 생성
            if (user.getAuthorities() == null) { // 아무 권한이 없다면
                return;
            } else if (user.getAuthorities().contains(targetRole)) { // 해당 권한을 가지고 있다면
                Set<Authority> authorities = user.getAuthorities()
                    .stream()
                    .filter(auth -> !auth.equals(targetRole)).collect(Collectors.toSet());
                user.setAuthorities(authorities);
                userRepository.save(user);
            }
        });
    }
*/