package com.mukcha.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum Authority {

    USER("ROLE_USER", "유저"),
    ADMIN("ROLE_ADMIN", "관리자");

    private final String key;
    private final String title;

}
/*
// @Entity
// @Builder
// @NoArgsConstructor
// @AllArgsConstructor
// @IdClass(Authority.class) // 중복된 authority가 부여되면 안되기 때문에 Idclass 지정
// @Table(name = "authority")
// public static final String ROLE_USER = ;
// public static final String ROLE_ADMIN = ;
// @Id
// private Long userId;
// @Id
// private String authority;
*/