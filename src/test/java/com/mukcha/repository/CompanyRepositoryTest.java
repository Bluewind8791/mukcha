package com.mukcha.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mukcha.domain.Company;
import com.mukcha.repository.helper.WithRepositoryTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles("local")
public class CompanyRepositoryTest extends WithRepositoryTest {

    @BeforeEach
    void before() {
        prepareTest();
    }


    @Test
    void testFindByName() {
        // when
        Company findCompany = companyRepository.findByName("testCompany").get();
        // then
        assertEquals("testCompany", findCompany.getName());
        assertEquals("testCompanyLogo", findCompany.getImage());
    }


}