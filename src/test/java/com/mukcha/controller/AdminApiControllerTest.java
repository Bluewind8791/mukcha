package com.mukcha.controller;

import com.mukcha.controller.dto.CompanyRequestDto;
import com.mukcha.controller.dto.FoodSaveRequestDto;
import com.mukcha.controller.dto.FoodUpdateRequestDto;
import com.mukcha.controller.helper.WithControllerTest;
import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminApiControllerTest extends WithControllerTest {

    @Test
    @DisplayName("1. 메뉴를 새로 생성한다")
    void test_1() {
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
        String url = "http://localhost:" + port + "/api/menus";
        // when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertThat(responseEntity.getBody()).isGreaterThan(0L);
        Food food = foodRepository.findByName("testName").get();
        assertEquals(foodName, food.getName());
        assertEquals(foodImage, food.getImage());
        assertEquals(Category.CHICKEN, food.getCategory());
        assertEquals(companyName, food.getCompany().getName());
        System.out.println(">>> "+food.toString());
    }

    @Test
    @DisplayName("2. 메뉴를 수정한다")
    void test_2() {
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
        String url = "http://localhost:" + port + "/api/menus/" + updateId;
        HttpEntity<FoodUpdateRequestDto> httpEntity = new HttpEntity<FoodUpdateRequestDto>(requestDto);
        // when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, Long.class);
        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertThat(responseEntity.getBody()).isGreaterThan(0L);
        Food editedFood = foodRepository.findById(updateId).get();
        assertEquals("testName2", editedFood.getName());
        assertEquals("testImage2", editedFood.getImage());
        System.out.println(editedFood.toString());
    }

    @Test
    @DisplayName("3. 메뉴를 삭제한다")
    void test_3() {
        // given
        Company savedCompany = createCompany("testCompany", "testLogo");
        Food savedFood = createFood(savedCompany, "testName", "testImage", Category.CHICKEN);
        Long targetId = savedFood.getFoodId();
        String url = "http://localhost:" + port + "/api/menus/{foodId}";
        Map<String, Long> params = new HashMap<>();
        params.put("foodId", targetId);
        // when
        restTemplate.delete(url, params);
        // then
        assertEquals(Optional.empty(), foodRepository.findById(targetId));
    }

    @Test
    @DisplayName("4. 회사를 생성한다")
    void testSaveCompany() {
        // given
        String companyName = "testName";
        String companyLogo = "testLogo";
        CompanyRequestDto companyRequestDto = CompanyRequestDto.builder()
            .companyName(companyName)
            .companyLogo(companyLogo)
            .build();
        String url = "http://localhost:" + port + "/api/companies";
        // when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, companyRequestDto, Long.class);
        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);
        Company company = companyRepository.findByName("testName").get();
        assertEquals(companyName, company.getName());
        assertEquals(companyLogo, company.getImage());
    }

    @Test
    @DisplayName("4. 회사의 정보를 수정한다")
    void testEditCompany() {
        // given
        Company savedCompany = createCompany("testName", "testLogo");
        Long updateId = savedCompany.getCompanyId();
        String expectName = "testName2";
        String expectLogo = "testLogo2";
        CompanyRequestDto requestDto = CompanyRequestDto.builder()
            .companyName(expectName)
            .companyLogo(expectLogo)
            .build();
        String url = "http://localhost:" + port + "/api/companies/" + updateId;
        HttpEntity<CompanyRequestDto> requestEntity = new HttpEntity<CompanyRequestDto>(requestDto);
        // when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);
        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertThat(responseEntity.getBody()).isGreaterThan(0L);
        Company company = companyRepository.findById(updateId).get();
        assertEquals(expectName, company.getName());
        assertEquals(expectLogo, company.getImage());
    }

    @Test
    @DisplayName("5. 회사를 삭제한다")
    void test_5() {
        // given
        Company savedCompany = createCompany("testCompany", "testLogo");
        createFood(savedCompany, "testName", "testImage", Category.CHICKEN);
        Long targetId = savedCompany.getCompanyId();
        String url = "http://localhost:" + port + "/api/companies/{companyId}";
        Map<String, Long> params = new HashMap<>();
        params.put("companyId", targetId);
        // when
        restTemplate.delete(url, params);
        // then
        assertEquals(Optional.empty(), companyRepository.findById(targetId));
    }


}
