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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.Builder;


@Getter
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

    private Score score;

    @Column(name = "likes_count", columnDefinition = "integer default 0")
    private int likesCount;

    @Column(name = "eaten_date")
    private LocalDate eatenDate; // updateable

    // review -> food (연관관계 주인)
    @ManyToOne
    @JoinColumn(name = "food_id", nullable = true)
    private Food food;

    // review -> user (연관관계 주인)
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;


    public void update(Score score, String comment) {
        this.score = score;
        this.comment = comment;
    }

    public void setEatenDate(LocalDate eatenDate) {
        this.eatenDate = eatenDate;
    }

    public void setFoodAndUser(User user, Food food) {
        this.food = food;
        this.user = user;
    }

    public void setFoodToNull() {
        this.food = null;
    }

    public void setUserToNull() {
        this.user = null;
    }

}