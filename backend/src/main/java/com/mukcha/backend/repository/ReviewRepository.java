package com.mukcha.backend.repository;

import com.mukcha.backend.domain.Review;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    
}
