package com.mukcha.controller.dto;

import javax.validation.constraints.NotBlank;

import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class FoodSaveRequestDto {

    @NotBlank(message = "메뉴 이름은 필수 입력 값입니다.")
    @Schema(description = "메뉴 이름", required = true, example = "꿀꿀햄버거")
    private String foodName;

    @Schema(description = "메뉴 이미지 URL", required = false, example = "none")
    private String foodImage;

    @NotBlank(message = "카테고리는 필수 입력 값입니다.")
    @Schema(description = "해당 메뉴의 카테고리", required = true, example = "HAMBURGER")
    private String category;

    @NotBlank(message = "회사명은 필수 입력 값입니다.")
    @Schema(description = "해당 메뉴의 회사명", required = true, example = "먹챠푸드")
    private String companyName;


    @Builder
    public FoodSaveRequestDto(String foodName, String foodImage, String category, String companyName) {
        this.foodName = foodName;
        this.foodImage = foodImage;
        this.category = category;
        this.companyName = companyName;
    }

    public Food toEntity(Company companyEntity) {
        return Food.builder()
            .name(foodName)
            .image(foodImage)
            .category(transCategory(category))
            .company(companyEntity)
            .build();
    }

    private Category transCategory(String category) {
        String upperCategory = category.toUpperCase();
        Category arr[] = Category.values();
        for (Category c : arr) {
            // 동일한 카테고리가 존재한다면
            if (upperCategory.equals(c.toString())) {
                return c;
            }
        }
        // 해당 카테고리가 존재하지 않을 시
        return null;
    }

    @Override
    public String toString() {
        return ">>> foodName: "+this.foodName
            +" /foodImage: "+this.foodImage
            +" /category: "+this.category
            +" /companyName: "+this.companyName
        ;
    }

}
