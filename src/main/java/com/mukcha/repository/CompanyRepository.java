package com.mukcha.repository;


import java.util.Optional;

import com.mukcha.domain.Company;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {

    // 회사 이름으로 db 에서 찾기
    Optional<Company> findByName(String name);

}
