package com.mukcha.backend.service;

import java.time.LocalDate;
import java.util.HashSet;

import com.mukcha.backend.domain.Authority;
import com.mukcha.backend.domain.Gender;
import com.mukcha.backend.domain.User;
import com.mukcha.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 회원가입 서비스
     * username, email 중복 불가
     */
    public User join(User user) {
        validateDuplicateNickname(user);
        validateDuplicateEmail(user);

        return userRepository.save(user);
    }

    public void updateEmail(Long userId, String email) {
        userRepository.findByEmail(email)
            .ifPresent(m -> {
                throw new IllegalStateException("이미 사용중인 이메일입니다.");
            });
        userRepository.updateEmail(userId, email);
    }

    public void updateNickname(Long userId, String nickname) {
        userRepository.findByNickname(nickname)
            .ifPresent(m -> {
                throw new IllegalStateException("이미 사용중인 아이디입니다.");
            });
        userRepository.updateNickname(userId, nickname);
    }

    public void updateProfileImage(Long userId, String profileImage) {
        userRepository.updateProfileImage(userId, profileImage);
    }
    public void updateGender(Long userId, Gender gender ) {
        userRepository.updateGender(userId, gender);
    }
    public void updateBirthday(Long userId, LocalDate birthday) {
        userRepository.updateBirthday(userId, birthday);
    }


    // 권한 부여
    public void addAuthority(Long userId, String authority) {
        userRepository.findById(userId).ifPresent(user -> {
            Authority newRole = new Authority(userId, authority);
            if (user.getAuthorities() == null) { // 아무 권한이 없다면
                HashSet<Authority> authorities = new HashSet<>();
                authorities.add(newRole);
                user.setAuthorities(authorities);
                join(user);
            } else if (user.getAuthorities().contains(newRole)) { // 해당 권한만 가지고 있지 않다면
                HashSet<Authority> authorities = new HashSet<>();
                authorities.addAll(user.getAuthorities()); // 기존의 권한에서
                authorities.add(newRole); // new role 추가
                user.setAuthorities(authorities);
                join(user);
            }
        });
    }


    // email 중복 검사
    private void validateDuplicateEmail(User user) {
        // 본인의 이메일이 전과 같다면 return
        User beforeEmail = userRepository.findById(user.getUserId()).get();
        if (beforeEmail.getEmail() == user.getEmail()) {
            return ;
        }
        userRepository.findByEmail(user.getEmail())
            .ifPresent(m -> {
                throw new IllegalStateException("이미 사용중인 이메일입니다.");
            });
    }
    // username 중복 검사
    private void validateDuplicateNickname(User user) {
        User before = userRepository.findById(user.getUserId()).get();
        if (before.getNickname() == user.getNickname()) {
            return ;
        }
        userRepository.findByNickname(user.getNickname())
            .ifPresent(m -> {
                throw new IllegalStateException("이미 사용중인 아이디입니다.");
            });
    }



}
