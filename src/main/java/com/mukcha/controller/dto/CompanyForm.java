package com.mukcha.controller.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;


@Data
public class CompanyForm {

    @NotBlank(message = "회사명은 필수 입력 값입니다.")
    private String companyName;

    private String companyLogo;

}
