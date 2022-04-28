package com.mukcha.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import com.mukcha.domain.Company;
import com.mukcha.repository.helper.WithRepositoryTest;

import org.assertj.core.api.Assertions;
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

    @Test
    void testBaseTimeEntity() {
        // given
        LocalDateTime now = LocalDateTime.of(2021, 4, 28, 0,0,0);
        companyRepository.save(Company.builder()
            .name("test")
            .image("image")
            .build()
        );

        // when
        Company company = companyRepository.findAll().get(0);

        // then
        System.out.println(">>> createDate = " + company.getCreatedAt());
        System.out.println(">>> updateDate = " + company.getUpdatedAt());
        Assertions.assertThat(company.getCreatedAt()).isAfter(now);
        Assertions.assertThat(company.getUpdatedAt()).isAfter(now);
    }


}