package com.mukcha.config;

import java.util.List;

import com.mukcha.domain.Food;
import com.mukcha.repository.FoodRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

// for test init DB
@Component
@RequiredArgsConstructor
public class DBInitForAverageScore implements CommandLineRunner {

    private final FoodRepository foodRepository;

    @Override
    public void run(String... args) throws Exception {

        List<Food> foods = foodRepository.findAll();
        for (Food food : foods) {
            food.setAverageScore();
            foodRepository.save(food);
        }
        System.out.println(">>> Set Food's average scores");

    }

}