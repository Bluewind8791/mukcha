package com.mukcha.config.dto;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200", 
        description = "응답에 성공하였습니다."
    ),
    @ApiResponse(
        responseCode = "400", 
        description = "잘못된 요청입니다."
    ),
    @ApiResponse(
        responseCode = "405",
        description = "권한이 없습니다. 로그인 후 이용해 주세요."
    )
})
public @interface SpringDocApiResponse {}
