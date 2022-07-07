package com.mukcha.controller;


import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.core.env.Environment;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@Tag(name = "Profile Controller")
public class ProfileController {

    private final Environment env;

    @GetMapping("/profile")
    @Operation(description = "실행중인 프로젝트의 Profile을 확인할 수 있는 API입니다.")
    public String getProfile() {
        List<String> profile = Arrays.asList(env.getActiveProfiles());
        List<String> realProfiles = Arrays.asList("set1", "set2");
        String defaultProfile = profile.isEmpty() ? "default" : profile.get(0);

        return profile.stream()
                .filter(realProfiles::contains)
                .findAny()
                .orElse(defaultProfile);
    }

}