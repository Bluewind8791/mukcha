package com.mukcha.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.HashMap;
import java.util.Map;

import com.mukcha.config.dto.LoginUser;
import com.mukcha.config.dto.SessionUser;
import com.mukcha.controller.dto.SessionUserResponseDto;
import com.mukcha.service.FoodService;
import com.mukcha.service.ReviewService;
import com.mukcha.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/menus")
public class FoodController {

    private final FoodService foodService;
    private final ReviewService reviewService;
    private final UserService userService;


    // 해당 메뉴의 정보와 리뷰 3개를 보여주는 페이지
    @GetMapping(value = "/{foodId}")
    public ModelAndView getMenu(
        @PathVariable Long foodId,
        @LoginUser SessionUser sessionUser,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "size", defaultValue = "3") Integer size
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put("_links",
            linkTo(methodOn(FoodController.class).getMenu(foodId, sessionUser, pageNum, size)).withSelfRel()
        );
        if (sessionUser != null) {
            SessionUserResponseDto user = userService.getSessionUserInfo(sessionUser);
            response.put("loginUser", user);
            // 로그인 한 유저가 리뷰를 적었다면
            if (reviewService.isUserWriteReviewOnFood(foodId, user.getUserId())) {
                response.put("isReviewed", "true");
                response.put("writtenReview", 
                    reviewService.findDtoByFoodIdAndUserId(foodId, user.getUserId())
                );
            } else { // 로그인 한 유저가 리뷰를 적지 않았다면
                response.put("isReviewed", "false");
            }
        }
        // 해당 메뉴의 정보
        response.put("food", foodService.findFood(foodId));
        // paging 처리한 해당 메뉴의 모든 리뷰 보기
        response.put("reviewList", 
            reviewService.findAllByFoodIdOrderByCreatedAtDesc(foodId, pageNum, size)
        );
        return new ModelAndView("food/detail", response, HttpStatus.OK);
    }

    // '리뷰 더보기'를 눌러 해당 메뉴의 모든 리뷰 보기
    @GetMapping(value = "/{foodId}/reviews")
    public ModelAndView getReviews(@PathVariable Long foodId, @LoginUser SessionUser sessionUser) {
        Map<String, Object> response = new HashMap<>();
        response.put("_links",
            linkTo(methodOn(FoodController.class).getReviews(foodId, sessionUser)).withSelfRel()
        );
        if (sessionUser != null) {
            response.put("loginUser", userService.getSessionUserInfo(sessionUser));
        }
        response.put("reviewList", reviewService.findAllByFoodId(foodId)); // 메뉴의 모든 리뷰 리스트
        return new ModelAndView("food/reviews", response, HttpStatus.OK);
    }

}
