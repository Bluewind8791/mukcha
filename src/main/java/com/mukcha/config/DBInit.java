package com.mukcha.config;

import java.util.Optional;
import java.util.Set;

import com.mukcha.domain.Authority;
import com.mukcha.domain.User;
import com.mukcha.service.UserService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

// for test init DB
@Component
@RequiredArgsConstructor
public class DBInit implements CommandLineRunner {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        userService.findUser(1L).or(() ->{
            User admin = User.builder()
                            .email("admin@test.com")
                            .nickname("admin_ben")
                            .password(passwordEncoder.encode("1234"))
                            .enabled(true)
                            .build();
            userService.save(admin);
            admin.setAuthorities(Set.of(
                new Authority(admin.getUserId(), Authority.ROLE_USER),
                new Authority(admin.getUserId(), Authority.ROLE_ADMIN)
            ));
            return Optional.of(userService.save(admin));
        });

        userService.findUser(1L).or(() ->{
            User user = User.builder()
                            .email("user@test.com")
                            .nickname("ben")
                            .password(passwordEncoder.encode("1234"))
                            .enabled(true)
                            .build();
            userService.save(user);
            user.setAuthorities(Set.of(
                new Authority(user.getUserId(), Authority.ROLE_USER)
            ));
            return Optional.of(userService.save(user));
        });
    }


}
