package com.mukcha.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.mukcha.repository.ReviewRepository;
import com.mukcha.controller.dto.CategoryDto;
import com.mukcha.controller.dto.ReviewResponseDto;
import com.mukcha.controller.dto.ReviewSaveRequestDto;
import com.mukcha.domain.Category;
import com.mukcha.domain.ErrorMessage;
import com.mukcha.domain.Review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final UserService userService;
    private final FoodService foodService;


    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    // 코멘트와 점수 저장 및 수정
    public Long saveReview(Long foodId, String userEmail, ReviewSaveRequestDto dto) {
        try {
            Review review = findByFoodIdAndEmail(foodId, userEmail);
            // update
            review.update(dto.getScore(), dto.getComment());
            log.info(">>> 리뷰가 수정되었습니다. "+review);
            return review.getReviewId();
        } catch (IllegalArgumentException e) {
            Review savedReview = dto.toEntity();
            savedReview.setFoodAndUser(userService.findByEmail(userEmail), foodService.findFood(foodId));
            save(savedReview);
            log.info(">>> 리뷰가 생성되었습니다. "+savedReview);
            return savedReview.getReviewId();
        }
    }

    private Review findByFoodIdAndEmail(Long foodId, String userEmail) {
        return reviewRepository.findAllByFoodId(foodId).stream().filter(r -> r.getUser().getEmail().equals(userEmail))
            .findFirst().orElseThrow(() -> 
                new IllegalArgumentException(ErrorMessage.REVIEW_NOT_FOUND.getMessage()+"/foodId="+foodId+"/email"+userEmail)
            );
    }

    // 먹은 날짜를 저장 및 수정
    public Review saveEatenDate(Long foodId, String email, String eatenDate) {
        // String eatenDate -> LocalDate 로 변환 (yyyy-mm-dd)
        String[] date = eatenDate.split("-");
        LocalDate eatenDateLD = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        Review review = findByFoodIdAndEmail(foodId, email);
        review.setEatenDate(eatenDateLD);
        return save(review);
    }

    // Review 삭제
    @Transactional
    public void deleteReview(Long foodId, String userEmail) {
        // 현재 로그인한 유저가 작성한 해당 음식의 리뷰를 찾는다.
        Review review = findByFoodIdAndEmail(foodId, userEmail);
        // Food 와 User 와의 관계를 null 시킨다.
        review.setUserToNull();
        review.setFoodToNull();
        save(review);
        reviewRepository.deleteById(review.getReviewId());
        log.info(">>> 해당 리뷰가 삭제 처리되었습니다."+review.toString());
    }






    /* FIND METHODS */

    public Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() -> 
            new IllegalArgumentException(ErrorMessage.REVIEW_NOT_FOUND.getMessage()+reviewId)
        );
    }

    @Transactional(readOnly = true)
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    // 해당 음식의 모든 리뷰 찾기
    @Transactional(readOnly = true)
    public List<Review> findAllReviewByFoodId(Long foodId) {
        return reviewRepository.findAllByFoodId(foodId);
    }

    public List<ReviewResponseDto> findAllReviewByFoodIdIntoDto(Long foodId) {
        List<ReviewResponseDto> dtos = new ArrayList<>();
        reviewRepository.findAllByFoodId(foodId).stream().forEach(r -> {
            ReviewResponseDto dto = new ReviewResponseDto(r);
            dtos.add(dto);
        });
        return dtos;
    }

    // 유저 아이디와 음식 아이디를 통하여 리뷰 찾기
    public Review findByFoodIdAndUserId(Long foodId, Long userId) {
        List<Review> foodReviews = findAllReviewByFoodId(foodId);
        return foodReviews.stream().filter(
            r -> r.getUser().getUserId().equals(userId)).findFirst().orElseThrow(
                () -> new IllegalArgumentException(ErrorMessage.REVIEW_NOT_FOUND.getMessage())
            );
    }

    // 유저 아이디와 음식 아이디를 통하여 리뷰 찾기
    public ReviewResponseDto findByFoodIdAndUserIdIntoDto(Long foodId, Long userId) {
        // 해당 메뉴의 리뷰 리스트
        List<Review> foodReviews = findAllReviewByFoodId(foodId);
        Review reviewFilterByUserId = foodReviews.stream().filter(
            r -> r.getUser().getUserId().equals(userId)).findFirst().get();
        return new ReviewResponseDto(reviewFilterByUserId);
    }

    // 해당 유저의 모든 리뷰 찾기
    @Transactional(readOnly = true)
    public List<Review> findAllByUserId(Long userId) {
        return reviewRepository.findAllByUserId(userId);
    }

    // 해당 메뉴의 모든 리뷰를 페이징 처리하여 가져온다
    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> findAllByFoodIdOrderByCreatedAtDesc(Long foodId, int pageNum, int size) {
        return findAllByFoodIdOrderByCreatedAtDesc(foodId, PageRequest.of(pageNum-1, size));
    }

    public Page<ReviewResponseDto> findAllByFoodIdOrderByCreatedAtDesc(Long foodId, Pageable pageable) {
        Page<Review> rPage = reviewRepository.findAllByFoodIdOrderByCreatedDesc(foodId, pageable);
        int totalElements = (int) rPage.getTotalElements();
        return new PageImpl<ReviewResponseDto>(rPage.getContent().stream()
            .map(r -> new ReviewResponseDto(r)).collect(Collectors.toList()), pageable, totalElements
        );
    }

    // 해당 유저가 쓴 리뷰 중 해당 카테고리의 모든 리뷰를 가져온다
    @Transactional(readOnly = true)
    public List<Review> getReviewByCategoryAndUserId(Long userId, Category category) {
        List<Review> reviews = findAllByUserId(userId);
        return reviews.stream().filter(r -> r.getFood().getCategory() == category).collect(Collectors.toList());
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

    // 해당 유저가 해당 메뉴의 리뷰를 적었는지 확인
    public boolean isUserWriteReviewOnFood(Long foodId, Long userId) {
        // 해당 메뉴의 리뷰 리스트
        List<Review> foodReviews = findAllReviewByFoodId(foodId);
        if (foodReviews.stream().anyMatch(r -> r.getUser().getUserId().equals(userId))) {
            return true;
        } else {
            return false;
        }
    }


}

