package com.mukcha.repository;

import java.util.List;

import com.mukcha.domain.Review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r from Review r where food.foodId=?1")
    List<Review> findAllByFoodId(Long foodId);

    @Query("select r from Review r where food.foodId=?1 order by r.createdAt desc")
    Page<Review> findAllByFoodIdOrderByCreatedDesc(Long foodId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("delete from Review r where r.food.foodId=?1")
    void deleteAllByFoodId(Long foodId);

    @Query("select r from Review r where user.userId=?1")
    List<Review> findAllByUserId(Long userId);

}
