package com.mukcha.repository;

import java.util.Optional;

import com.mukcha.domain.Gender;
import com.mukcha.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    
    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    @Modifying(clearAutomatically = true)
    @Query("update User set email=?2 where userId=?1")
    void updateEmail(Long userId, String email);

    @Modifying(clearAutomatically = true)
    @Query("update User set nickname=?2 where userId=?1")
    void updateNickname(Long userId, String nickname);

    @Modifying(clearAutomatically = true)
    @Query("update User set gender=?2 where userId=?1")
    void updateGender(Long userId, Gender gender);

    @Modifying(clearAutomatically = true)
    @Query("update User set birthYear=?2 where userId=?1")
    void updateBirthday(Long userId, String birthYear);

    @Modifying(clearAutomatically = true)
    @Query("update User set profileImage=?2 where userId=?1")
    void updateProfileImage(Long userId, String profileImage);

    @Modifying(clearAutomatically = true)
    @Query("update User set password=?2 where userId=?1")
    void updatePassword(Long userId, String password);

    // 회원 탈퇴
    @Modifying(clearAutomatically = true)
    @Query("update User set enabled=false where userId=?1")
    void disableUser(Long userId);


}


/*
@Modifying
이 Annotation은 @Query Annotation으로 작성 된 변경, 삭제 쿼리 메서드를 사용할 때 필요
즉, 데이터에 변경이 일어나는 INSERT, UPDATE, DELETE, DDL 에서 사용

JPA Entity LifeCycle을 무시하고 쿼리가 실행되기 때문에 해당 annotation을 사용할 때는 영속성 콘텍스트 관리에 주의

벌크 연산이란 다건의 UPDATE, DELETE 연산을 하나의 쿼리로 하는 것
@Query에 벌크 연산 쿼리를 작성하고, @Modifying을 붙이지 않으면, InvalidDataAccessApiUsage exception이 발생합니다.

clearAutomatically
이 Attribute는 @Modifying 이 붙은 해당 쿼리 메서드 실행 직후, 역속성 컨텍스트를 clear 할 것인지를 지정한다
default 값은 false 입니다. 하지만 default 값인 false를 그대로 사용할 경우, 영속성 컨텍스트의 1차 캐시와 관련한 문제점이 발생할 수 있다.

출처 : https://devhyogeon.tistory.com/4
*/