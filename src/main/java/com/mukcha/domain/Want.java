package com.mukcha.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "want")
public class Want {
    
    @Id
    @Column(name = "want_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wantId;

    @Column(name = "food_id")
    private Long foodId;

}
