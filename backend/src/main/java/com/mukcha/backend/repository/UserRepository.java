package com.mukcha.backend.repository;

import com.mukcha.backend.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
