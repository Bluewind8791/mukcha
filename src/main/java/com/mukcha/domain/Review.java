package com.mukcha.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import groovy.transform.builder.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.AllArgsConstructor;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Review extends BaseTimeEntity {

    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private String comment;

    @NonNull
    private Score score;

    @Column(name = "likes_count", columnDefinition = "integer default 0")
    private int likesCount;

    @Column(name = "eaten_date")
    private LocalDate eatenDate; // updateable

    // review -> food (연관관계 주인)
    @ManyToOne
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    // review -> user (연관관계 주인)
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;


    public void setFoodToNull() {
        this.food = null;
    }

    public void setUserToNull() {
        this.user = null;
    }

}


/**
    public void setFood(Food food) {
        // 기존 리뷰와의 관계 제거
        if (this.food != null) {
            this.food.getReviews().remove(this);
        }
        this.food = food;
        food.getReviews().add(this);
    }
*/