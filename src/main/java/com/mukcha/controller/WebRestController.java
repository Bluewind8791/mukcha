package com.mukcha.controller;


import java.util.Arrays;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.env.Environment;

import lombok.AllArgsConstructor;


@RestController
@AllArgsConstructor
public class WebRestController {

    private Environment env;

    // 실행중인 프로젝트의 Profile이 뭔지 확인할 수 있는 API
    @GetMapping("/profile")
    public String getProfile() {
        System.out.println(">>> "+
            Arrays.toString(env.getActiveProfiles()).replace("[", "").replace("]", "")
        );
        return Arrays.toString(env.getActiveProfiles()).replace("[", "").replace("]", "");
    }

}
