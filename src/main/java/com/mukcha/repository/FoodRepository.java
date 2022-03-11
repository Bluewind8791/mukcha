package com.mukcha.repository;

import java.util.List;
import java.util.Optional;

import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface FoodRepository extends JpaRepository<Food, Long> {

    Optional<Food> findByName(String foodName);

    @Query("select distinct(category) from Food")
    List<String> getAllCategories();

    List<Food> findAllByCategory(Category category);

    List<Food> findAllByCompany(Company company);

    @Modifying
    @Transactional
    void deleteById(Long foodId);

}
