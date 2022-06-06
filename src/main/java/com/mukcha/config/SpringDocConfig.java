package com.mukcha.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;


@Configuration
@SecurityScheme(
    name = "security_auth",
    type = SecuritySchemeType.OAUTH2,
    flows = @OAuthFlows(
            authorizationCode = @OAuthFlow(
                authorizationUrl = "${springdoc.oAuthFlow.authorizationUrl}", 
                tokenUrl = "${springdoc.oAuthFlow.tokenUrl}"
            )
        )
)
public class SpringDocConfig {

    @Bean
    public OpenAPI openAPI(@Value("${springdoc.version}") String version) {
        Info info = new Info()
            .title("MUKCHA API Documentation")
            .version(version)
            .description("MUKCHA 웹 어플리케이션의 API 문서입니다.")
            .contact(new Contact().email("bluewind@kakao.com").name("Bluewind Kim"))
        ;
        return new OpenAPI()
            .info(info);
    }

}