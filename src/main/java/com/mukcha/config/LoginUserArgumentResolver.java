package com.mukcha.config;

import javax.servlet.http.HttpSession;

import com.mukcha.config.dto.LoginUser;
import com.mukcha.config.dto.SessionUser;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final HttpSession httpSession;

    /**
     * 메서드가 특정 파라미터를 지원하는지 판단
     * 1. @LoginUser annotation이 붙어있는지
     * 2. 파라미터 클래스타입이 SessionUser.class 인지
     * -> 1 && 2 ? true : false
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
        boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());
        return isLoginUserAnnotation && isUserClass;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter, 
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, 
            WebDataBinderFactory binderFactory
        ) throws Exception {
        return httpSession.getAttribute("user"); // 세션에서 유저 객체 정보를 가져온다
    }
    
}
