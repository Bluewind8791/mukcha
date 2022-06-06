package com.mukcha.config;

import javax.sql.DataSource;

import com.mukcha.domain.Authority;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@EnableWebSecurity(debug = false) // spring security 설정 활성화
@EnableGlobalMethodSecurity(prePostEnabled = true) // prepost 로 권한 체크
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;
    private final CustomOAuth2UserService customOAuth2UserService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // for h2-console
            .formLogin().disable()
            .headers().frameOptions().disable() // for h2-console
            .and()
            .authorizeRequests(request -> {
                request
                    .antMatchers("/").permitAll()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/h2-console/**").permitAll()
                    .antMatchers("/admin/**").hasRole(Authority.ADMIN.name())
                    .antMatchers("/user/**").hasAnyRole(Authority.USER.name(), Authority.ADMIN.name())
                    .antMatchers("/api/**").hasAnyRole(Authority.USER.name(), Authority.ADMIN.name())
                    .antMatchers("/api/admin/**").hasRole(Authority.ADMIN.name())
                    .anyRequest().permitAll();
            })
            .oauth2Login(login -> {
                login.loginPage("/login"); // 로그인 페이지 커스텀
                // oauth2 로그인 성공 후 사용자 정보를 가져올 때의 설정
                login.userInfoEndpoint()
                    .userService(customOAuth2UserService); // 로그인 성공 시 후속 조치를 진행할 userService 구현체
            })
            .logout(logout -> {
                logout.logoutSuccessUrl("/");
            })
            .rememberMe(service -> {
                service.rememberMeServices(rememberMeServices());
            })
            .exceptionHandling(denied -> {
                denied.accessDeniedPage("/access-denied");
            })
        ;
    }

    /**
     * The request was rejected because the URL contained a potentially malicious String "//"
     * 더블 슬래쉬 허용 FireWall로 변환
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.httpFirewall(defaltHttpFireWall());
    }


    // >>> BEANS <<<
    @Bean
    public PersistentTokenBasedRememberMeServices rememberMeServices() {
        PersistentTokenBasedRememberMeServices rememberMeServices = new PersistentTokenBasedRememberMeServices(
            "rememberme-key", userDetailsService(), tokenRepository()
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

    @Bean
    public HttpFirewall defaltHttpFireWall() {
        return new DefaultHttpFirewall();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user")
                .password("{noop}pass")
                .roles("USER")
        ;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
/*
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new LoginSuccessHandler();
    }
    @Bean
    public AuthenticationFailureHandler failureHandler() {
        return new LoginFailureHandler();
    }
*/