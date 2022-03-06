package com.mukcha.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "food")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Food extends BaseTimeEntity {

    @Id
    @Column(name = "food_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodId;

    private String name;

    private String image;

    @Enumerated(EnumType.STRING)
    private Category category;

    // food -> company (연관관계 주인)
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;


    // food -> review (readOnly)
    @JsonIgnore // json 변환 시, 무한루프 방지
    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "food", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();


    // public void addReviews(Review review) {
    //     this.reviews.add(review);
    //     // 무한루프 방지
    //     if (review.getFood() != this) {
    //         review.setFood(this);
    //     }
    // }

    public float getAverageScore() {
        List<Integer> scoreList = getReviews().stream().map(r -> r.getScore().value).collect(Collectors.toList());
        if (scoreList.isEmpty()) {
            return 0;
        }
        int total = scoreList.stream().mapToInt(Integer::intValue).sum();
        return (total / (float)scoreList.size());
    }




}