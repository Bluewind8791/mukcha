package com.mukcha.backend.service.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mukcha.backend.domain.Company;
import com.mukcha.backend.service.CompanyService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CompanyTestHelper {

    private final CompanyService companyService;

    public Company makeCompany(String name, String imageUrl) {
        return Company.builder()
                    .name(name)
                    .image(imageUrl)
                    .build();
    }

    public Company createCompany(String name, String imageUrl) {
        return companyService.save(makeCompany(name, imageUrl));
    }

    public void assertCompany(Company company, String name, String imageUrl) {
        assertNotNull(company.getName());
        assertNotNull(company.getImage());
        assertNotNull(company.getCreatedAt());
        assertNotNull(company.getUpdatedAt());

        assertEquals(name, company.getName());
        assertEquals(imageUrl, company.getImage());
    }


}
