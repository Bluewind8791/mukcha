package com.mukcha.backend.service.helper;

import java.util.stream.Stream;

import com.mukcha.backend.domain.Authority;
import com.mukcha.backend.domain.User;
import com.mukcha.backend.service.UserService;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserTestHelper {
    
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public static User makeUser(String email, String nickname) {
        return User.builder()
                   .email(email)
                   .nickname(nickname)
                   .enabled(true)
                   .build();
    }

    public User createUser(String email, String nickname) {
        User user = makeUser(email, nickname);
        user.setPassword(passwordEncoder.encode(nickname+"123"));
        return userService.join(user);
    }

    public User createUser(String email, String nickname, String ... authorities) {
        User user = createUser(email, nickname);
        Stream.of(authorities).forEach(auth -> userService.addAuthority(user.getUserId(), auth));
        return user;
    }

    public User createAdmin(String email, String nickname) {
        User admin = createUser(email, nickname);
        userService.addAuthority(admin.getUserId(), Authority.ROLE_ADMIN);
        return admin;
    }



}
