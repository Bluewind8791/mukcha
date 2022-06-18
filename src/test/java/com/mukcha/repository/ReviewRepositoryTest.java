package com.mukcha.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.mukcha.domain.Review;
import com.mukcha.domain.Score;
import com.mukcha.repository.helper.WithRepositoryTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@SpringBootTest
@ActiveProfiles("local")
public class ReviewRepositoryTest extends WithRepositoryTest {

    @BeforeEach
    void before() {
        prepareTest();
        this.review = repositoryTestHelper.createReview(food, user, "testComment", Score.GOOD);
    }


    @Test
    void testFindAllByFoodId() {
        assertEquals("testComment", review.getComment());
    }

    @Test
    void testDeleteAllByFoodId() {
        // when
        reviewRepository.deleteAllByFoodId(food.getFoodId());
        // then
        assertEquals(List.of(), reviewRepository.findAllByFoodId(food.getFoodId()));
    }

    @Test
    void testFindAllByFoodIdOrderByCreatedDesc() {
        Page<Review> pagingReview = reviewRepository.findAllByFoodIdOrderByCreatedDesc(food.getFoodId(), PageRequest.of(0, 1));
        Review testReview = pagingReview.get().findFirst().get();
        assertEquals("testComment", testReview.getComment());
    }

    @Test
    void testFindAllByUserId() {
        List<Review> reviews = reviewRepository.findAllByUserId(user.getUserId());
        Review testReview = reviews.get(0);
        assertEquals("testComment", testReview.getComment());
    }

}