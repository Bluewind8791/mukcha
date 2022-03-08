package com.mukcha.controller.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FoodDto {

    private Long foodId;

    @NotBlank(message = "메뉴 이름은 필수 입력 값입니다.")
    private String foodName;

    @NotBlank(message = "카테고리는 필수 입력 값입니다.")
    private String category;

    @NotBlank(message = "회사명은 필수 입력 값입니다.")
    private String company;

    private String foodImage;

    private double averageScore;

}
