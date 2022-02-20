package com.mukcha.backend.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Service;

@Service
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
        ) throws IOException, ServletException {

        // 강제 intercept 당했을 때 데이터 획득
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        // 로그인 버튼을 눌러 접속했을 경우 전에 있었던 페이지 정보 획득
        String prevPage = (String) request.getSession().getAttribute("prevPage");

        // url 기본 값
        String url = "/";

        // 이전 페이지가 접근 제한 페이지일 경우
        if (savedRequest != null) {
            url = savedRequest.getRedirectUrl();

        } else if (prevPage != null) { // 로그인 버튼을 눌러 접근한 경우
            url = prevPage;
        }

        // 로그인 완료시 세션 저장


        // redirect
        response.sendRedirect(url);
    }


}
