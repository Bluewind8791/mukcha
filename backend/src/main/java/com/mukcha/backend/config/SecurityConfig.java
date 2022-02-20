package com.mukcha.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


// @EnableGlobalMethodSecurity(prePostEnabled = true) // prepost 로 권한 체크
@EnableWebSecurity(debug = false) // filter 확인
public class SecurityConfig extends WebSecurityConfigurerAdapter {



    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .csrf().disable()
            .formLogin(login -> {
                login.loginPage("/login");
            })
            .logout(logout -> {
                logout.logoutSuccessUrl("/");
            })
            .authorizeRequests(request -> {
                request.anyRequest().permitAll();
            })
        ;
        // http
        //     .authorizeRequests(request -> {
        //         request
        //             .anyRequest().permitAll() // permit all
        //             // .anyRequest().authenticated() // 나머지 모든것에 auth 적용
        //         ;
        //     }


    }
}
