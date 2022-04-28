package com.mukcha.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.mukcha.domain.Score;
import com.mukcha.repository.FoodRepository;
import com.mukcha.repository.ReviewRepository;
import com.mukcha.repository.UserRepository;
import com.mukcha.controller.dto.CategoryDto;
import com.mukcha.domain.Category;
import com.mukcha.domain.ErrorMessage;
import com.mukcha.domain.Review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;


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
    public Optional<Review> findReviewByFoodIdAndUserId(Long foodId, Long userId) {
        List<Review> reviewFoundByFoodId = findAllReviewByFoodId(foodId);
        Optional<Review> reviewFilterByUserId = reviewFoundByFoodId.stream().filter(r -> 
            r.getUser().getUserId().equals(userId)).findFirst();
        return reviewFilterByUserId;
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
                case TTEOKBOKKI:
                    categoryDto.setTteokReviewCount(
                        reviews.stream().filter(r -> r.getFood().getCategory().equals(c)).collect(Collectors.toList()).size()
                    );
                case PASTA:
                    categoryDto.setTteokReviewCount(
                        reviews.stream().filter(r -> r.getFood().getCategory().equals(c)).collect(Collectors.toList()).size()
                    );
                case SIDEMENU:
                    categoryDto.setTteokReviewCount(
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
        review.editScore(score);
    }
    public void editReviewComment(Review review, String comment) {
        review.editComment(comment);
    }
    public void editReviewEatenDate(Review review, LocalDate eatenDate) {
        review.setEatenDate(eatenDate);
    }


    /* DELETE METHODS */
    // Review 삭제
    @Transactional
    public void deleteReview(Long foodId, Long userId) {
        // 현재 로그인한 유저가 작성한 해당 음식의 리뷰를 찾는다.
        Review review = findReviewByFoodIdAndUserId(foodId, userId).orElseThrow(() ->
            new IllegalArgumentException(ErrorMessage.REVIEW_NOT_FOUND.getMessage()+"/foodId="+foodId+"/userId"+userId)
        );
        // review 삭제를 위해서는 Food 와 User 와의 관계를 null 시켜야 한다.
        review.setUserToNull();
        review.setFoodToNull();
        save(review);
        reviewRepository.deleteById(review.getReviewId());
        log.info(">>> 해당 리뷰가 삭제 처리되었습니다."+review.toString());
    }


    // 점수 평가
    public Review saveReview(Score score, String comment, Long foodId, Long userId) {
        Review review;
        try {
            review = findReviewByFoodIdAndUserId(foodId, userId).orElseThrow(() ->
                new IllegalArgumentException(ErrorMessage.REVIEW_NOT_FOUND.getMessage()+"/foodId="+foodId+"/userId"+userId)
            );
            review.editScore(score);
            review.editComment(comment);
        } catch (Exception e) {
            review = Review.builder()
                .food(foodRepository.findById(foodId).get())
                .user(userRepository.findById(userId).get())
                .score(score)
                .comment(comment)
                .build();
        }
        return save(review);
    }

    // 먹은 날짜를 저장하는 메소드
    public Review saveEatenDate(String eatenDate, Long foodId, Long userId) {
        // String eatenDate -> LocalDate 로 변환 (yyyy-mm-dd)
        String[] date = eatenDate.split("-");
        LocalDate eatenDateLD = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        Review review;
        try {
            review = findReviewByFoodIdAndUserId(foodId, userId).orElseThrow(() ->
                new IllegalArgumentException(ErrorMessage.REVIEW_NOT_FOUND.getMessage()+"/foodId="+foodId+"/userId"+userId)
            );
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

    // 해당 유저가 쓴 리뷰 중 해당 카테고리의 모든 리뷰를 가져온다
    @Transactional(readOnly = true)
    public List<Review> getReviewByCategoryAndUserId(Long userId, Category category) {
        List<Review> reviews = findAllByUserId(userId);
        return reviews.stream().filter(r -> r.getFood().getCategory() == category).collect(Collectors.toList());
    }





}

