package com.mukcha.domain;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(Authority.class) // 중복된 authority가 부여되면 안되기 때문에 Idclass 지정
public class Authority implements GrantedAuthority {
    
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    @Id
    private Long userId;

    @Id
    private String authority;
}