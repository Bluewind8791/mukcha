package com.mukcha.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "company")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Company extends BaseTimeEntity {

    @Id
    @Column(name = "company_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;

    private String name;

    private String image;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "company", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private List<Food> foods = new ArrayList<>();


    public void deleteFood(Food food) {
        this.foods.remove(food);
    }

    public void editCompanyImageUrl(String imageUrl) {
        this.image = imageUrl;
    }

    public void editCompanyName(String companyName) {
        this.name = companyName;
    }

    public void update(String companyName, String companyLogo) {
        this.name = companyName;
        this.image = companyLogo;
    }


}
