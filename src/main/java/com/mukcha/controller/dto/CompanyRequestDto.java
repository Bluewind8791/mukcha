package com.mukcha.controller.dto;

import javax.validation.constraints.NotBlank;

import com.mukcha.domain.Company;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class CompanyRequestDto {

    @NotBlank(message = "회사명은 필수 입력 값입니다.")
    @Schema(description = "회사명", required = true, example = "먹챠푸드")
    private String companyName;

    @Schema(description = "회사 로고 URL", required = false, example = "http://ec2-3-39-16-219.ap-northeast-2.compute.amazonaws.com/logo/logo.png")
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

    @Override
    public String toString() {
        return ">>> companyName: "+this.companyName+" /companyLogo: "+this.companyLogo;
    }


}
