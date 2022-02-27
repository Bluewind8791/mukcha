package com.mukcha.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.mukcha.domain.Authority;
import com.mukcha.domain.Gender;
import com.mukcha.domain.User;
import com.mukcha.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;


@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    // 회원가입 서비스
    public User save(User user) {
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


    // finding services
    public Optional<User> findUser(Long userId) {
        return userRepository.findById(userId);
    }
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public Optional<User> findByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }


    // update 서비스
    public void updateEmail(Long userId, String email) {
        userRepository.updateEmail(userId, email);
    }
    public void updateNickname(Long userId, String nickname) {
        userRepository.updateNickname(userId, nickname);
    }
    public void updateProfileImage(Long userId, String profileImage) {
        userRepository.updateProfileImage(userId, profileImage);
    }
    public void updateGender(Long userId, Gender gender) {
        userRepository.updateGender(userId, gender);
    }
    public void updateBirthday(Long userId, LocalDate birthday) {
        userRepository.updateBirthday(userId, birthday);
    }
    public void updatePassword(Long userId, String password) {
        userRepository.updatePassword(userId, password);
    }


    // 권한 부여
    public void addAuthority(Long userId, String authority) {
        userRepository.findById(userId).ifPresent(user -> {
            Authority newRole = new Authority(user.getUserId(), authority);
            if (user.getAuthorities() == null) { // 아무 권한이 없다면
                HashSet<Authority> authorities = new HashSet<>();
                authorities.add(newRole);
                user.setAuthorities(authorities);
                save(user);
            } else if (!user.getAuthorities().contains(newRole)) { // 해당 권한만 가지고 있지 않다면
                HashSet<Authority> authorities = new HashSet<>();
                authorities.addAll(user.getAuthorities()); // 기존의 권한에서
                authorities.add(newRole); // new role 추가
                user.setAuthorities(authorities);
                save(user);
            }
        });
    }

    public void removeAuthority(Long userId, String authority) {
        userRepository.findById(userId).ifPresent(user -> {
            Authority targetRole = new Authority(user.getUserId(), authority); // target role 생성
            if (user.getAuthorities() == null) { // 아무 권한이 없다면
                return;
            } else if (user.getAuthorities().contains(targetRole)) { // 해당 권한을 가지고 있다면
                Set<Authority> authorities = user.getAuthorities()
                    .stream()
                    .filter(
                        auth -> !auth.equals(targetRole))
                            .collect(Collectors.toSet());

                user.setAuthorities(authorities);
                save(user);
            }
        });
    }











    // email 중복 검사
    // private void validateDuplicateEmail(User user) {
    //     // 본인의 이메일이 전과 같다면 return
    //     User before = userRepository.findByEmail(user.getEmail()).get();
    //     if (before.getEmail() == user.getEmail()) {
    //         return ;
    //     }
    //     userRepository.findByEmail(user.getEmail())
    //         .ifPresent(m -> {
    //             throw new IllegalStateException("이미 사용중인 이메일입니다.");
    //         });
    // }
    // // username 중복 검사
    // private void validateDuplicateNickname(User user) {
    //     User before = userRepository.findByNickname(user.getNickname()).get();
    //     if (before.getNickname() == user.getNickname()) {
    //         return ;
    //     }
    //     userRepository.findByNickname(user.getNickname())
    //         .ifPresent(m -> {
    //             throw new IllegalStateException("이미 사용중인 아이디입니다.");
    //         });
    // }



}
