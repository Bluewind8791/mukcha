package com.mukcha.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.mukcha.domain.Category;
import com.mukcha.domain.Review;

import lombok.Getter;


@Getter
public class CategoryCountResponseDto {

    private int chickenReviewCount;
    private int pizzaReviewCount;
    private int burgerReviewCount;
    private int tteokReviewCount;
    private int pastaReviewCount;
    private int sideReviewCount;


    public CategoryCountResponseDto(List<Review> reviews) {
        this.chickenReviewCount = reviews.stream().filter(r -> 
            r.getFood().getCategory().equals(Category.CHICKEN)).collect(Collectors.toList()).size();
        this.pizzaReviewCount = reviews.stream().filter(r -> 
            r.getFood().getCategory().equals(Category.PIZZA)).collect(Collectors.toList()).size();
        this.burgerReviewCount = reviews.stream().filter(r -> 
            r.getFood().getCategory().equals(Category.HAMBURGER)).collect(Collectors.toList()).size();
        this.tteokReviewCount = reviews.stream().filter(r -> 
            r.getFood().getCategory().equals(Category.TTEOKBOKKI)).collect(Collectors.toList()).size();
        this.pastaReviewCount = reviews.stream().filter(r -> 
            r.getFood().getCategory().equals(Category.PASTA)).collect(Collectors.toList()).size();
        this.sideReviewCount = reviews.stream().filter(r -> 
            r.getFood().getCategory().equals(Category.SIDEMENU)).collect(Collectors.toList()).size();
    }

}
