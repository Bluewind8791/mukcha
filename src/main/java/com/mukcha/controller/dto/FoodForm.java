package com.mukcha.controller.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;


@Data
public class FoodForm {

    @NotBlank(message = "메뉴 이름은 필수 입력 값입니다.")
    private String foodName;

    private String foodImage;

    @NotBlank(message = "카테고리는 필수 입력 값입니다.")
    private String category;

    @NotBlank(message = "회사명은 필수 입력 값입니다.")
    private String company;

}
