package com.mukcha.domain;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;


public class Search {

    public static Specification<Food> foodSearching(String keyword) {
        return new Specification<Food>() {
            @Override
            public Predicate toPredicate(Root<Food> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(root.get("name"), "%"+keyword+"%");
            }
        };
    }

    public static Specification<User> userSearching(String keyword) {
        return (Specification<User>) ((root, query, builder) -> 
                builder.like(root.get("nickname"), "%"+keyword+"%")
        );
    }

    public static Specification<Company> companySearching(String keyword) {
        return (Specification<Company>) ((root, query, builder) -> 
                builder.like(root.get("name"), "%"+keyword+"%")
        );
    }


}
