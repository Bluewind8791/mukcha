package com.mukcha.controller;

import java.util.List;

import com.mukcha.controller.dto.UserDto;
import com.mukcha.domain.Category;
import com.mukcha.domain.User;
import com.mukcha.service.FoodService;
import com.mukcha.service.ReviewService;
import com.mukcha.service.UserService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class HomeController {

    private final FoodService foodService;
    private final UserService userService;
    private final ReviewService reviewService;

    // root page
    @GetMapping(value = {"/", ""})
    public String home(Model model) {
        model.addAttribute("avgScoreTopTen", foodService.findTopTenOrderByScore());
        model.addAttribute("newestTopTen", foodService.findTopTenNewest());
        return "home";
    }

    // login page
    @GetMapping("/login")
    public String login(
            @RequestParam(value = "error", defaultValue = "false") Boolean error,
            Model model
        ) {
        model.addAttribute("error", error);
        return "user/loginForm";
    }

    // 유저 정보 페이지
    @GetMapping(value = "/users/{userId}")
    public String viewUserInfo(
        @PathVariable Long userId,
        Model model,
        @AuthenticationPrincipal User principal
    ) {
        // Category List
        List<Category> categoryList = List.of(Category.values());
        model.addAttribute("categoryList", categoryList);

        User user = userService.findUser(userId).orElseThrow(() -> 
            new IllegalArgumentException("해당 유저를 찾을 수 없습니다.")
        );
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setNickname(user.getNickname());
        userDto.setProfileImage(user.getProfileImage());
        model.addAttribute("user", userDto);

        model.addAttribute("reviewCount", reviewService.getReviewCountByCategoryAndUserId(userId));
        return "user/userPage";
    }

    @GetMapping(value = "/users/{userId}/category/{category}")
    public String viewReviewInCategory(
        @PathVariable Long userId,
        @PathVariable Category category,
        Model model
    ) {
        model.addAttribute("reviewList", 
            reviewService.getReviewByCategoryAndUserId(userId, category)
        );
        return "food/reviews";
    }


}
