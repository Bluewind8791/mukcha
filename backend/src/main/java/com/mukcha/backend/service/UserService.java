package com.mukcha.backend.service;

import com.mukcha.backend.domain.User;
import com.mukcha.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 회원가입 서비스
     * 이메일 중복 불가
     */
    public Long join(User user) {
        validateDuplicateUsername(user);
        validateDuplicateEmail(user);
        
        userRepository.save(user);
        return user.getId();
    }

    /**
     * 회원 수정 서비스
     */
    public Long editUserInfo(User user) {
        validateDuplicateEmail(user);
        validateDuplicateUsername(user);
        
        userRepository.save(user);
        return user.getId();
    }





    private void validateDuplicateEmail(User user) {
        userRepository.findByEmail(user.getEmail())
            .ifPresent(m -> {
                throw new IllegalStateException("이미 사용중인 이메일입니다.");
            });
    }
    private void validateDuplicateUsername(User user) {
        userRepository.findByUsername(user.getUsername())
            .ifPresent(m -> {
                throw new IllegalStateException("이미 사용중인 아이디입니다.");
            });
    }

}
