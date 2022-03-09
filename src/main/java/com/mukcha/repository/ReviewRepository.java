package com.mukcha.repository;

import java.util.List;

import com.mukcha.domain.Review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r from Review r where food.foodId=?1")
    List<Review> findAllByFoodId(Long foodId);

    @Query("select r from Review r where food.foodId=?1 order by r.createdAt desc")
    Page<Review> findAllByFoodIdOrderByCreatedDesc(Long foodId, Pageable pageable);

}
