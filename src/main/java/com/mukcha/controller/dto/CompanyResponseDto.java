package com.mukcha.controller.dto;

import java.time.LocalDateTime;

import com.mukcha.domain.Company;

import lombok.Getter;

@Getter
public class CompanyResponseDto {

    private Long companyId;
    private String companyName;
    private String companyLogo;
    private LocalDateTime companyUpdatedAt;

    public CompanyResponseDto(Company entity) {
        this.companyId = entity.getCompanyId();
        this.companyName = entity.getName();
        this.companyLogo = entity.getImage();
        this.companyUpdatedAt = entity.getUpdatedAt();
    }

}
