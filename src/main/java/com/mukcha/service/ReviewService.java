package com.mukcha.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.mukcha.domain.Score;
import com.mukcha.domain.User;
import com.mukcha.repository.ReviewRepository;
import com.mukcha.domain.Food;
import com.mukcha.domain.Review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }


    public Review save(Review review) {
        return reviewRepository.save(review);
    }


    /* FIND METHODS */
    public Optional<Review> findReview(Long reviewId) {
        return reviewRepository.findById(reviewId);
    }
    // 해당 음식의 모든 리뷰 찾기
    @Transactional(readOnly = true)
    public List<Review> findAllReviewByFoodId(Long foodId) {
        return reviewRepository.findAllByFoodId(foodId);
    }
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }
    // 유저 아이디와 음식 아이디를 통하여 리뷰 찾기
    public Review findReviewByFoodIdAndUserId(Long foodId, Long userId) {
        List<Review> reviewFoundByFoodId = findAllReviewByFoodId(foodId);
        Optional<Review> reviewFilterByUserId = reviewFoundByFoodId.stream().filter(r -> r.getUser().getUserId().equals(userId)).findFirst();
        return reviewFilterByUserId.get();
    }


    /* EDIT METHODS */
    public void editReviewScore(Review review, Score score) {
        review.setScore(score);
    }
    public void editReviewComment(Review review, String comment) {
        review.setComment(comment);
    }
    public void editReviewEatenDate(Review review, LocalDate eatenDate) {
        review.setEatenDate(eatenDate);
    }


    /* DELETE METHODS */
    // Review 삭제
    public void deleteReview(Review review) {
        // review 삭제를 위해서는 Food 와 User 와의 관계를 null 시켜야 한다.
        review.setUserToNull();
        review.setFoodToNull();
        save(review);
        reviewRepository.deleteById(review.getReviewId());
    }


    // 점수 평가
    public Review saveReview(Score score, String comment, Food food, User user) {
        Review review;
        try {
            review = findReviewByFoodIdAndUserId(food.getFoodId(), user.getUserId());
            review.setScore(score);
            review.setComment(comment);
        } catch (Exception e) {
            review = new Review();
            review.setFood(food);
            review.setUser(user);
            review.setScore(score);
            review.setComment(comment);
        }
        save(review);
        return review;
    }

    // 먹은 날짜
    public Review saveEatenDate(String eatenDate, Food food, User user) {
        // String eatenDate -> LocalDate 로 변환 (yyyy-mm-dd)
        String[] date = eatenDate.split("-");
        LocalDate eatenDateLD = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        Review review;
        try {
            review = findReviewByFoodIdAndUserId(food.getFoodId(), user.getUserId());
            review.setEatenDate(eatenDateLD);
            save(review);
            return review;
        } catch (Exception e) {
            throw new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다.");
        }
    }

    @Transactional(readOnly = true)
    public Page<Review> findAllByFoodIdOrderByCreatedAtDesc(Long foodId, int pageNum, int size) {
        return reviewRepository.findAllByFoodIdOrderByCreatedDesc(foodId, PageRequest.of(pageNum-1, size));
    }

    // public void setReviewComment(Review review, String comment) {
    //     if (review.getScore() == null) {
    //         throw new IllegalArgumentException("점수를 먼저 매겨주세요!");
    //     }
    //     review.setComment(comment);
    // }
    // public void setReviewEatenDate(Review review, LocalDate eatenDate) {
    //     if (review.getScore() == null) {
    //         throw new IllegalArgumentException("점수를 먼저 매겨주세요!");
    //     }
    //     review.setEatenDate(eatenDate);
    // }



}

