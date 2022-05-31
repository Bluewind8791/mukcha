package com.mukcha.controller.dto;

import com.mukcha.domain.Food;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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

    public boolean equals(Food food) {
        if (food.getName().equals(this.foodName)) {
            if (food.getImage().equals(this.foodImage)) {
                if (food.getCategory().toString().equals(this.category))
                    return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return ">>> foodName = "+foodName+
            " /foodImage = "+foodImage+
            " /category = "+ category
        ;
    }

}
