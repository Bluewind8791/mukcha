package com.mukcha.repository;

import java.util.List;
import java.util.Optional;

import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface FoodRepository extends JpaRepository<Food, Long>, JpaSpecificationExecutor<Food> {

    Optional<Food> findByName(String foodName);

    List<Food> findAllByCategory(Category category);

    List<Food> findAllByCompany(Company company);

    @Query("select distinct(category) from Food")
    List<String> findAllCategories();

    @Modifying
    @Transactional
    void deleteById(Long foodId);

    List<Food> findAllByCompanyAndCategory(Company company, Category category);

    // 가장 최신의 메뉴 10개
    List<Food> findTop10ByOrderByUpdatedAtDesc();

    // 가장 평균점수가 높은 메뉴 10개
    List<Food> findTop10ByOrderByAverageScoreDesc();

}