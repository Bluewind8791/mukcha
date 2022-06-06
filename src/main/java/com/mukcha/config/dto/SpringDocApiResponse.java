package com.mukcha.config.dto;

import java.lang.annotation.Documented;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@Documented
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200", 
        description = "OK"
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
