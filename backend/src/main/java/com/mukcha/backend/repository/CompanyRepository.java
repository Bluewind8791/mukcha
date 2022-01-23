package com.mukcha.backend.repository;

import com.mukcha.backend.domain.Company;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    
}
