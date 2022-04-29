package com.mukcha.controller.dto;

import javax.validation.constraints.NotBlank;

import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class FoodSaveRequestDto {

    @NotBlank(message = "메뉴 이름은 필수 입력 값입니다.")
    private String foodName;

    private String foodImage;

    @NotBlank(message = "카테고리는 필수 입력 값입니다.")
    private String category;

    @NotBlank(message = "회사명은 필수 입력 값입니다.")
    private String companyName;

    private Company companyEntity;

    @Builder
    public FoodSaveRequestDto(String foodName, String foodImage, String category, String companyName) {
        this.foodName = foodName;
        this.foodImage = foodImage;
        this.category = category;
        this.companyName = companyName;
    }

    public Food toEntity() {
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

    public void setCompanyEntity(Company company) {
        this.companyEntity = company;
    }

}
