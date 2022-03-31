package com.mukcha.service.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mukcha.domain.Authority;
import com.mukcha.domain.User;
import com.mukcha.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;

public class UserTestHelper {
    
    @Autowired private UserService userService;

    public UserTestHelper(UserService userService) {
        this.userService = userService;
    }

    public User makeUser(String email, String nickname) {
        return User.builder()
            .email(email)
            .nickname(nickname)
            .authority(Authority.USER)
            .enabled(true)
            .build()
        ;
    }

    public User createUser(String email, String nickname) {
        User user = makeUser(email, nickname);
        return userService.signUp(user);
    }

    public void assertUser(User user, String email, String nickname) {
        assertNotNull(user.getEmail());
        assertNotNull(user.getNickname());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
        assertTrue(user.isEnabled());

        assertEquals(email, user.getEmail());
        assertEquals(nickname, user.getNickname());
    }


}

/*
스트림은 '데이터의 흐름'입니다. 
배열 또는 컬렉션 인스턴스에 함수 여러 개를 조합해서 원하는 결과를 필터링하고 가공된 결과를 얻을 수 있습니다. 
또한 람다를 이용해서 코드의 양을 줄이고 간결하게 표현할 수 있습니다. 
즉, 배열과 컬렉션을 함수형으로 처리할 수 있습니다.

스트림에 대한 내용은 크게 세 가지로 나눌 수 있습니다.

1. 생성하기 : 스트림 인스턴스 생성.
2. 가공하기 : 필터링(filtering) 및 맵핑(mapping) 등 원하는 결과를 만들어가는 중간 작업(intermediate operations).
3. 결과 만들기 : 최종적으로 결과를 만들어내는 작업(terminal operations).
*/