package com.mukcha.config;

import java.util.Collections;

import javax.servlet.http.HttpSession;

import com.mukcha.config.dto.SessionUser;
import com.mukcha.domain.Authority;
import com.mukcha.domain.User;
import com.mukcha.repository.UserRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 어떤 SNS 로그인인지 구별하기 위한 ID ("google", "naver")
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest
            .getClientRegistration()
            .getProviderDetails()
            .getUserInfoEndpoint()
            .getUserNameAttributeName() // OAuth2 로그인 진행시 키가되는 필드 값 (PK)
        ;

        // OAuth2UserService를 통해 가져온 OAuth2User의 attribute
        OAuthAttributes attributes = OAuthAttributes.of(
            registrationId,
            userNameAttributeName,
            oAuth2User.getAttributes()
        );
        User user = saveOrUpdate(attributes);
        httpSession.setAttribute("user", new SessionUser(user)); // 세션에 사용자 정보를 저자하기위한 dto 클래스

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getAuthorityKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }


    // 유저 생성 및 수정 서비스 로직
    private User saveOrUpdate(OAuthAttributes attributes){        
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(u -> u.update(attributes.getName(), attributes.getPicture(), null, null))
                .orElse(attributes.toEntity())
        ;
        if (user.getEmail().equals("castus1214@naver.com")) {
            log.info(">>> 'castus1214@naver.com'에게 관리자 권한을 부여합니다.");
            user.setAuthority(Authority.ADMIN);
        }
        if (user.getEmail().equals("bluewind@kakao.com")) {
            log.info(">>> 'bluewind@kakao.com'에게 관리자 권한을 부여합니다.");
            user.setAuthority(Authority.ADMIN);
        }
        log.info("회원 <"+user.getEmail()+">님이 회원가입 하였습니다.");
        return userRepository.save(user);
    }


}
