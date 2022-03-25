package com.mukcha.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.mukcha.domain.Score;
import com.mukcha.domain.User;
import com.mukcha.repository.ReviewRepository;
import com.mukcha.controller.dto.CategoryDto;
import com.mukcha.domain.Category;
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
    @Transactional(readOnly = true)
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }
    // 유저 아이디와 음식 아이디를 통하여 리뷰 찾기
    public Review findReviewByFoodIdAndUserId(Long foodId, Long userId) {
        List<Review> reviewFoundByFoodId = findAllReviewByFoodId(foodId);
        Optional<Review> reviewFilterByUserId = reviewFoundByFoodId.stream().filter(r -> r.getUser().getUserId().equals(userId)).findFirst();
        return reviewFilterByUserId.get();
    }
    // 해당 유저의 모든 리뷰 찾기
    @Transactional(readOnly = true)
    public List<Review> findAllByUserId(Long userId) {
        return reviewRepository.findAllByUserId(userId);
    }

    // 해당 유저의 리뷰를 카테고리 별로 가져오기
    @Transactional(readOnly = true)
    public CategoryDto getReviewCountByCategoryAndUserId(Long userId) {
        List<Review> reviews = findAllByUserId(userId);
        CategoryDto categoryDto = new CategoryDto();

        for (Category c : Category.values()) {
            switch (c) {
                case CHICKEN:
                    categoryDto.setChickenReviewCount(
                        reviews.stream().filter(r -> r.getFood().getCategory().equals(c)).collect(Collectors.toList()).size()
                    );
                case PIZZA:
                    categoryDto.setPizzaReviewCount(
                        reviews.stream().filter(r -> r.getFood().getCategory().equals(c)).collect(Collectors.toList()).size()
                    );
                case HAMBURGER:
                    categoryDto.setBurgerReviewCount(
                        reviews.stream().filter(r -> r.getFood().getCategory().equals(c)).collect(Collectors.toList()).size()
                    );
                default:
                    break;
            }
        }
        return categoryDto;
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

    @Transactional(readOnly = true)
    public List<Review> getReviewByCategoryAndUserId(Long userId, Category category) {
        List<Review> reviews = findAllByUserId(userId);
        return reviews.stream().filter(r -> r.getFood().getCategory() == category).collect(Collectors.toList());
    }





}

