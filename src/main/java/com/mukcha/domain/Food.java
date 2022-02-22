package com.mukcha.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Food extends BaseTimeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodId;

    private String name;
    
    private String image;
    
    @Enumerated(EnumType.STRING)
    private Category category;

    // 한 회사에는 여러가지의 메뉴/음식이 있다.
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "food_id"))
    Company company;

    // 한 음식마다 여러 리뷰가 있다.
    @Builder.Default
    @OneToMany(mappedBy = "food")
    private List<Review> reviews = new ArrayList<>();

}
