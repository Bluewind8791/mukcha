package com.mukcha.controller.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CompanyDto {

    @NotBlank(message = "회사명은 필수 입력 값입니다.")
    private String companyName;

    private String companyLogo;

    private LocalDateTime companyUpdatedAt;

}
