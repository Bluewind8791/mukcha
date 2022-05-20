package com.mukcha.config;

import java.util.Map;

import com.mukcha.domain.Authority;
import com.mukcha.domain.Gender;
import com.mukcha.domain.User;

import lombok.Builder;
import lombok.Getter;


@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String email;
    private String name;
    private String picture;
    private String gender;
    private String birthyear;

    @Builder
    public OAuthAttributes(
            Map<String, Object> attributes,
            String nameAttributeKey,
            String name,
            String email,
            String picture,
            String gender,
            String birthyear
        ) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.gender = gender;
        this.birthyear = birthyear;
    }

    // OAuth2User에서 반환하는 사용자 정보는 Map이기 때문에 값 하나하나를 변환해야 합니다.
    public static OAuthAttributes of(
        String registrationId, 
        String userNameAttributeName,
        Map<String, Object> attributes
    ) {
        // naver
        if ("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }
        // google
        return ofGoogle(userNameAttributeName, attributes);
    }

    public User toEntity() {
        return User.builder()
                .email(email)
                .nickname(name)
                .profileImage(picture)
                .birthYear(birthyear)
                .gender(transClassGender(gender))
                .authority(Authority.USER)
                .enabled(true)
                .build();
    }


    @SuppressWarnings("unchecked")
    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        /*
        * JSON형태이기 떄문에 Map을 통해서 데이터를 가져온다.
        naverProfileResponse: {
            "resultcode":"00",
            "message":"success",
            "response":{
                "id":"",
                "nickname":"",
                "profile_image":"",
                "gender":"M",
                "email":",
                "birthday":"12-14",
                "birthyear":"1991"
            }
        }*/
        Map<String, Object> response = (Map<String, Object>)attributes.get("response");
        return OAuthAttributes.builder()
                .name((String)response.get("nickname"))
                .email((String)response.get("email"))
                .picture((String)response.get("profile_image"))
                .gender((String)response.get("gender"))
                .birthyear((String)response.get("birthyear"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build()
        ;
    }


    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build()
        ;
    }

    // 네이버 회원가입 성별 클래스 전환
    private static Gender transClassGender(String gender) {
        if (gender != null) {
            // naver - F: 여성 - M: 남성 - U: 확인불가
            // google - "male", "female"
            switch (gender) {
                case "F" :
                case "female":
                return Gender.FEMALE;
                case "M":
                case "male":
                return Gender.MALE;
            }
        }
        return null;
    }

}
