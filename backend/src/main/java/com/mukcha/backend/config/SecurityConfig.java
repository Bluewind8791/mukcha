package com.mukcha.backend.config;

import javax.sql.DataSource;

import com.mukcha.backend.service.UserSecurityService;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@EnableWebSecurity(debug = false) // filter 확인
@EnableGlobalMethodSecurity(prePostEnabled = true) // prepost 로 권한 체크
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserSecurityService userSecurityService;
    private final DataSource dataSource;


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
            .rememberMe(service -> {
                service.rememberMeServices(rememberMeServices());
            })
        ;
    }


    @Bean
    public PersistentTokenBasedRememberMeServices rememberMeServices() {
        PersistentTokenBasedRememberMeServices rememberMeServices = new PersistentTokenBasedRememberMeServices(
            "rememberme-key", userSecurityService, tokenRepository()
        );
        rememberMeServices.setParameter("remember-me");
        rememberMeServices.setTokenValiditySeconds(60*60*24); // one day
        rememberMeServices.setAlwaysRemember(false);
        return rememberMeServices;
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl repositoryImpl = new JdbcTokenRepositoryImpl();
        repositoryImpl.setDataSource(dataSource);
        try {
            repositoryImpl.removeUserTokens("username");
        } catch (Exception e) {
            repositoryImpl.setCreateTableOnStartup(true);
        }
        return repositoryImpl;
    }





}
