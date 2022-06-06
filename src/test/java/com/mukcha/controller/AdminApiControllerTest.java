package com.mukcha.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mukcha.controller.dto.CompanyRequestDto;
import com.mukcha.controller.dto.FoodSaveRequestDto;
import com.mukcha.controller.dto.FoodUpdateRequestDto;
import com.mukcha.controller.dto.SessionUserResponseDto;
import com.mukcha.controller.helper.WithMockMvcTest;
import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.domain.User;

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


@ActiveProfiles("test")
@WithMockUser(roles = "ADMIN")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AdminApiControllerTest extends WithMockMvcTest {

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
    @DisplayName("관리자 루트 페이지 접근")
    void urlTest() throws Exception {
        String url = "http://localhost:" + port + "/admin";
        mvc.perform(MockMvcRequestBuilders.get(url).requestAttr("loginUser", userResDto))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("메뉴를 새로 생성한다")

    void test_1() throws Exception {
        // given
        createCompany("testCompany", "testLogo");
        String foodName = "testName";
        String foodImage = "testImage";
        String category = "CHICKEN";
        String companyName = "testCompany";
        FoodSaveRequestDto requestDto = FoodSaveRequestDto.builder()
            .foodName(foodName)
            .foodImage(foodImage)
            .category(category)
            .companyName(companyName)
            .build()
        ;
        String url = "http://localhost:" + port + "/api/admin/menus";
        // when
        mvc.perform(MockMvcRequestBuilders.post(url)
                .requestAttr("loginUser", userResDto)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andDo(MockMvcResultHandlers.print())
        ;
        // then
        Food food = foodRepository.findByName("testName").get();
        assertEquals(foodName, food.getName());
        assertEquals(foodImage, food.getImage());
        assertEquals(Category.CHICKEN, food.getCategory());
        assertEquals(companyName, food.getCompany().getName());
        System.out.println(">>> "+food.toString());
    }

    @Test
    @DisplayName("메뉴를 수정한다")
    void test_2() throws Exception {
        // given
        Company savedCompany = createCompany("testCompany", "testLogo");
        Food savedFood = createFood(savedCompany, "testName", "testImage", Category.CHICKEN);
        Long updateId = savedFood.getFoodId();
        String expectName = "testName2";
        String expectImage = "testImage2";
        Category expectCategory = Category.HAMBURGER;
        FoodUpdateRequestDto requestDto = FoodUpdateRequestDto.builder()
            .foodName(expectName)
            .foodImage(expectImage)
            .category(expectCategory.name())
            .build();
        String url = "http://localhost:" + port + "/api/admin/menus/" + updateId;
        // when
        mvc.perform(MockMvcRequestBuilders
                .put(url)
                .requestAttr("loginUser", userResDto)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print());
        // then
        Food editedFood = foodRepository.findById(updateId).get();
        assertEquals("testName2", editedFood.getName());
        assertEquals("testImage2", editedFood.getImage());
        System.out.println(editedFood.toString());
    }

    @Test
    @DisplayName("회사를 생성한다")
    void testSaveCompany() throws JsonProcessingException, Exception {
        // given
        String companyName = "testName";
        String companyLogo = "testLogo";
        CompanyRequestDto companyRequestDto = CompanyRequestDto.builder()
            .companyName(companyName)
            .companyLogo(companyLogo)
            .build();
        String url = "http://localhost:" + port + "/api/admin/companies";
        // when
        mvc.perform(MockMvcRequestBuilders
                .post(url)
                .requestAttr("loginUser", userResDto)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(companyRequestDto)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andDo(MockMvcResultHandlers.print());
        // then
        Company company = companyRepository.findByName(companyName).get();
        assertEquals(companyName, company.getName());
        assertEquals(companyLogo, company.getImage());
    }

    @Test
    @DisplayName("회사의 정보를 수정한다")
    void testEditCompany() throws JsonProcessingException, Exception {
        // given
        Company savedCompany = createCompany("testName", "testLogo");
        Long updateId = savedCompany.getCompanyId();
        String expectName = "testName2";
        String expectLogo = "testLogo2";
        CompanyRequestDto requestDto = CompanyRequestDto.builder()
            .companyName(expectName)
            .companyLogo(expectLogo)
            .build();
        String url = "http://localhost:" + port + "/api/admin/companies/" + updateId;
        // when
        mvc.perform(MockMvcRequestBuilders
                .put(url)
                .requestAttr("loginUser", userResDto)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print());
        // then
        Company company = companyRepository.findById(updateId).get();
        assertEquals(expectName, company.getName());
        assertEquals(expectLogo, company.getImage());
    }

    @Test
    @DisplayName("회사를 삭제한다")
    void test_5() throws Exception {
        // given
        Company savedCompany = createCompany("testCompany", "testLogo");
        createFood(savedCompany, "testName", "testImage", Category.CHICKEN);
        Long targetId = savedCompany.getCompanyId();
        String url = "http://localhost:" + port + "/api/admin/companies/" + targetId;
        Map<String, Long> params = new HashMap<>();
        params.put("companyId", targetId);
        // when
        mvc.perform(MockMvcRequestBuilders
                .delete(url)
                .requestAttr("loginUser", userResDto))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print());
        // then
        assertEquals(Optional.empty(), companyRepository.findById(targetId));
    }


    @Test
    @DisplayName("메뉴를 삭제한다")
    void test_3() throws Exception {
        // given
        Company savedCompany = createCompany("testCompany", "testLogo");
        Food savedFood = createFood(savedCompany, "testName", "testImage", Category.CHICKEN);
        Long targetId = savedFood.getFoodId();
        String url = "http://localhost:" + port + "/api/admin/menus/" + targetId;
        // when
        mvc.perform(MockMvcRequestBuilders
                .delete(url)
                .requestAttr("loginUser", userResDto))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print());
        // then
        assertEquals(Optional.empty(), foodRepository.findById(targetId));
    }


}