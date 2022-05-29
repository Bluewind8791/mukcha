package com.mukcha.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mukcha.controller.dto.CompanyRequestDto;
import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.service.helper.WithTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@SpringBootTest
@ActiveProfiles("test")
public class CompanyServiceTest extends WithTest {

    @BeforeEach
    protected void before() {
        prepareTest();
    }


    @Test
    @DisplayName("회사의 이름, 이미지URL를 수정한다.")
    void test_4() {
        // set
        Company company = companyTestHelper.createCompany("test company", "companyLogo");
        // edit
        CompanyRequestDto requestDto = new CompanyRequestDto("ChickenPlus", "NewCompanyLogo");
        companyService.update(company.getCompanyId(), requestDto);
        // assert
        Company savedCompany = companyService.findByCompanyId(company.getCompanyId());
        assertEquals("ChickenPlus", savedCompany.getName());
        assertEquals("NewCompanyLogo", savedCompany.getImage());
    }

    @Test
    @DisplayName("회사를 삭제한다.")
    void test_5() {
        // set
        Company company = companyTestHelper.createCompany("test company", "companyLogo");
        Food food1 = foodTestHelper.createFood("food1", company, Category.CHICKEN, null);
        Long companyId = company.getCompanyId();
        System.out.println(">>> "+company.getFoods());
        // delete
        companyService.deleteCompany(companyId);
        // assert
        assertThrows(IllegalArgumentException.class, () -> companyService.findByCompanyId(companyId));
        assertNull(food1.getCompany());
    }


}