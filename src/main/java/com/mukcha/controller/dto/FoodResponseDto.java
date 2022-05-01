package com.mukcha.controller.dto;

import com.mukcha.domain.Food;

import lombok.Getter;


@Getter
public class FoodResponseDto {

    private Long foodId;
    private String foodName;
    private String foodImage;
    private String category;
    private Long companyId;
    private String companyName;
    private double averageScore;

    public FoodResponseDto(Food entity) {
        this.foodId = entity.getFoodId();
        this.foodName = entity.getName();
        this.foodImage = entity.getImage();
        this.category = entity.getCategory().name();
        this.companyId = entity.getCompany().getCompanyId();
        this.companyName = entity.getCompany().getName();
        if (!entity.getReviews().isEmpty()) {
            this.averageScore = getAverageScore(entity);
        }
    }

    private double getAverageScore(Food entity) {
        int sum = entity.getReviews().stream().map(r -> r.getScore().value)
            .mapToInt(Integer::intValue).sum();
        float average = sum / (float)entity.getReviews().size();
        return Math.round(average * 100) / 100.0;
    }

}
