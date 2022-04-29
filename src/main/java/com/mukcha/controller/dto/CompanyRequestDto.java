package com.mukcha.controller.dto;

import javax.validation.constraints.NotBlank;

import com.mukcha.domain.Company;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class CompanyRequestDto {

    @NotBlank(message = "회사명은 필수 입력 값입니다.")
    private String companyName;
    private String companyLogo;


    @Builder
    public CompanyRequestDto(String companyName, String companyLogo) {
        this.companyName = companyName;
        this.companyLogo = companyLogo;
    }

    public Company toEntity() {
        return Company.builder()
            .name(companyName)
            .image(companyLogo)
            .build();
    }


}
