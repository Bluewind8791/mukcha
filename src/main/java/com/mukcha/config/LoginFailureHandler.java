package com.mukcha.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;


public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final String DEFAULT_FAILURE_URL = "/login?error=true";

    @Override
    public void onAuthenticationFailure(
                HttpServletRequest request,
                HttpServletResponse response,
                AuthenticationException exception
        ) throws IOException, ServletException {

        String errorMessage = null;

        if (exception instanceof AuthenticationServiceException) {
            errorMessage = "존재하지 않는 사용자입니다.";
        } else if (exception instanceof BadCredentialsException) {
			errorMessage = "아이디 또는 비밀번호가 틀립니다.";
		} else if (exception instanceof LockedException) {
			errorMessage = "잠긴 계정입니다.";
		} else if (exception instanceof DisabledException) {
			errorMessage = "비활성화된 계정입니다.";
		} else if (exception instanceof AccountExpiredException) {
			errorMessage = "만료된 계정입니다.";
		} else if (exception instanceof CredentialsExpiredException) {
			errorMessage = "비밀번호가 만료되었습니다.";
		}
    
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher(DEFAULT_FAILURE_URL).forward(request, response);

        /*
		// 로그인 페이지로 다시 포워딩
		RequestDispatcher dispatcher = request.getRequestDispatcher("/login");
		dispatcher.forward(request, response);
        */
    }


}
