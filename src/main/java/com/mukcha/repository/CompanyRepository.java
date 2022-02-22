package com.mukcha.repository;


import com.mukcha.domain.Company;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface CompanyRepository extends JpaRepository<Company, Long> {

    // 회사 이름으로 db 에서 찾기
    Company findByName(String name);

    @Modifying(clearAutomatically = true)
    @Query("update Company set image=?2 where companyId=?1")
    void updateCompanyImage(Long companyId, String image);

    @Modifying(clearAutomatically = true)
    @Query("update Company set name=?2 where companyId=?1")
    void updateCompanyName(Long companyId, String name);



}
