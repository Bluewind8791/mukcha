package com.mukcha.controller.dto;

import com.mukcha.domain.Food;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FoodUpdateRequestDto {

    @Schema(description = "메뉴 이름", required = false, example = "꿀꿀햄버거")
    private String foodName;

    @Schema(description = "메뉴 이미지 URL", required = false, example = "none")
    private String foodImage;

    @Schema(description = "해당 메뉴의 카테고리", required = false, example = "HAMBURGER")
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
