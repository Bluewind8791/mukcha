package com.mukcha.controller.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FoodDto {

    private Long foodId;

    @NotBlank(message = "메뉴 이름은 필수 입력 값입니다.")
    private String foodName;

    private String foodImage;

    @NotBlank(message = "카테고리는 필수 입력 값입니다.")
    private String category;

    private double averageScore;

    private Long companyId;

    @NotBlank(message = "회사명은 필수 입력 값입니다.")
    private String companyName;

    private LocalDateTime createdAt;

}
