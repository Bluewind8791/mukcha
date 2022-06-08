package com.mukcha.crawling;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.mukcha.controller.dto.SessionUserResponseDto;
import com.mukcha.controller.helper.WithMockMvcTest;
import com.mukcha.domain.User;


@ActiveProfiles("test")
@WithMockUser(roles = "ADMIN")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CrawlingMockTest extends WithMockMvcTest {

    @BeforeEach
    void before() {
        prepareTest();
        User adminUser = createAdminUser("admin@test.com", "admin");
        this.userResDto = new SessionUserResponseDto(adminUser);
        mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
    }

    @Test
    @DisplayName("1. 배스킨라빈스 크롤링 테스트")
    void test_1() throws Exception {
        // given
        String url = "http://localhost:" + port + "/api/admin/crawling/selenium";
        // when
        mvc.perform(MockMvcRequestBuilders
            .post(url)
            .requestAttr("loginUser", userResDto)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print());
    }

}
