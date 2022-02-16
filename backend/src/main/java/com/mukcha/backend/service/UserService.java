package com.mukcha.backend.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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


    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findUser(Long userId) {
        return userRepository.findById(userId);
    }

    // 회원가입 서비스
    public User join(User user) {
        return userRepository.save(user);
    }

    // 수정 서비스
    public void updateEmail(Long userId, String email) {
        // User user = findUser(userId);
        userRepository.updateEmail(userId, email);
        // userRepository.save(user);
    }

    public void updateNickname(Long userId, String nickname) {
        // userRepository.findByNickname(nickname)
        //     .ifPresent(m -> {
        //         throw new IllegalStateException("이미 사용중인 아이디입니다.");
        //     });
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

    public void updatePassword(Long userId, String password) {
        if (password.length() <= 5 || password.length() > 20) {
            throw new IllegalArgumentException("패스워드는 6자 이상, 20자 이하여야 합니다");
        }
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
                join(user);
            } else if (!user.getAuthorities().contains(newRole)) { // 해당 권한만 가지고 있지 않다면
                HashSet<Authority> authorities = new HashSet<>();
                authorities.addAll(user.getAuthorities()); // 기존의 권한에서
                authorities.add(newRole); // new role 추가
                user.setAuthorities(authorities);
                join(user);
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
                join(user);
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
