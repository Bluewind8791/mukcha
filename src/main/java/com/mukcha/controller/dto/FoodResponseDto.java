package com.mukcha.controller.dto;

import com.mukcha.domain.Food;

import lombok.Getter;


@Getter
public class FoodResponseDto {

    private Long foodId;
    private String foodName;
    private String foodImage;
    private String category;
    private String companyName;

    public FoodResponseDto(Food entity) {
        this.foodId = entity.getFoodId();
        this.foodName = entity.getName();
        this.foodImage = entity.getImage();
        this.category = entity.getCategory().name();
        this.companyName = entity.getCompany().getName();
    }

}
