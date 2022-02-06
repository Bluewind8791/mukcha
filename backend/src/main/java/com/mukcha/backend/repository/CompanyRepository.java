package com.mukcha.backend.repository;

import com.mukcha.backend.domain.Company;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CompanyRepository extends JpaRepository<Company, Long> {

    // 회사 이름으로 db 에서 찾기
    Company findByName(String name);
    
}
