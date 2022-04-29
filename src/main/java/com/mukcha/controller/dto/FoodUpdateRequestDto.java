package com.mukcha.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FoodUpdateRequestDto {

    private String foodName;
    private String foodImage;
    private String category;

    @Builder
    public FoodUpdateRequestDto(String foodName, String foodImage, String category) {
        this.foodName = foodName;
        this.foodImage = foodImage;
        this.category = category;
    }

}
