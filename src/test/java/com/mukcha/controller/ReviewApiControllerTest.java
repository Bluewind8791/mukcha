package com.mukcha.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mukcha.controller.dto.EatenDateSaveRequestDto;
import com.mukcha.controller.dto.ReviewSaveRequestDto;
import com.mukcha.controller.dto.SessionUserResponseDto;
import com.mukcha.controller.dto.UserResponseDto;
import com.mukcha.controller.helper.WithMockMvcTest;
import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.domain.Review;
import com.mukcha.domain.Score;
import com.mukcha.domain.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@ActiveProfiles("test")
@WithMockUser(roles = "USER")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReviewApiControllerTest extends WithMockMvcTest {

    UserResponseDto userDto;

    @BeforeEach
    void before() {
        prepareTest();
        User user = createUser("user@test.com", "ben-user");
        this.userResDto = new SessionUserResponseDto(user);
        this.userDto = userService.findByUserId(user.getUserId());
        mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
    }

    @Test
    @DisplayName("MockMvc 테스트를 이용하여 USER 권한을 가지고 페이지 접근")
    void urlTest() throws Exception {
        String url = "http://localhost:" + port + "/user/edit";
        mvc.perform(MockMvcRequestBuilders
            .get(url)
            .requestAttr("loginUser", userResDto)
            .requestAttr("user", userDto)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("리뷰를 저장한다")
    void testSaveReview() throws JsonProcessingException, Exception {
        // given
        User reviewUser = userService.findUser(userResDto.getUserId());
        Company company = createCompany("testCompany", "companyLogo");
        Food food = createFood(company, "foodName", "foodImage", Category.CHICKEN);
        String comment = "comment";
        String rating = "또 먹고 싶어요";
        ReviewSaveRequestDto requestDto = ReviewSaveRequestDto.builder()
            .comment(comment)
            .rating(rating)
            .build();
        String url = "http://localhost:"+port+"/api/users/"+reviewUser.getUserId()+"/menus/"+food.getFoodId();
        // when
        mvc.perform(MockMvcRequestBuilders
            .post(url)
            .requestAttr("loginUser", userResDto)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(requestDto))
        ).andExpect(MockMvcResultMatchers.status().isOk());
        // then
        Review savedReview = reviewRepository.findAll().get(0);
        assertEquals(comment, savedReview.getComment());
        assertEquals(Score.GOOD, savedReview.getScore());
        assertEquals("foodName", savedReview.getFood().getName());
        assertEquals("ben-user", savedReview.getUser().getNickname());
    }

    @Test
    @DisplayName("리뷰를 수정한다.")
    void testUpdateReview() throws JsonProcessingException, Exception {
        // given - save
        User reviewUser = userService.findUser(userResDto.getUserId());
        Company company = createCompany("testCompany", "companyLogo");
        Food food = createFood(company, "foodName", "foodImage", Category.CHICKEN);
        createReview(reviewUser, food, "comment", Score.GOOD);
        // given - update
        String expectComment = "updated";
        Score expectScore = Score.BAD;
        ReviewSaveRequestDto requestDto = ReviewSaveRequestDto.builder()
            .comment(expectComment)
            .rating(expectScore.getRating())
            .build();
        String url = "http://localhost:"+port+"/api/users/"+reviewUser.getUserId()+"/menus/"+food.getFoodId();
        // when
        mvc.perform(MockMvcRequestBuilders
            .post(url)
            .requestAttr("loginUser", userResDto)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(requestDto))
        ).andExpect(MockMvcResultMatchers.status().isOk());
        // then
        Review savedReview = reviewRepository.findAll().get(0);
        assertEquals(expectComment, savedReview.getComment());
        assertEquals(expectScore, savedReview.getScore());
        assertEquals("foodName", savedReview.getFood().getName());
        assertEquals("ben-user", savedReview.getUser().getNickname());
    }

    @Test
    @DisplayName("먹은 날짜를 저장한다.")
    void testSaveEatenDate() throws JsonProcessingException, Exception {
        // given
        User reviewUser = userService.findUser(userResDto.getUserId());
        Company company = createCompany("testCompany", "companyLogo");
        Food food = createFood(company, "foodName", "foodImage", Category.CHICKEN);
        createReview(reviewUser, food, "comment", Score.GOOD);
        EatenDateSaveRequestDto requestDto = EatenDateSaveRequestDto.builder()
            .eatenDate(LocalDate.now().toString())
            .build();
        String url = "http://localhost:"+port+"/api/users/"+reviewUser.getUserId()+"/menus/"+food.getFoodId();
        // when
        mvc.perform(MockMvcRequestBuilders
            .put(url)
            .requestAttr("loginUser", userResDto)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(requestDto))
        ).andExpect(MockMvcResultMatchers.status().isOk());
        // then
        Review savedReview = reviewRepository.findAll().get(0);
        assertEquals(LocalDate.now(), savedReview.getEatenDate());
        assertEquals("comment", savedReview.getComment());
        assertEquals(Score.GOOD, savedReview.getScore());
        assertEquals("foodName", savedReview.getFood().getName());
        assertEquals("ben-user", savedReview.getUser().getNickname());
    }

    @Test
    @DisplayName("해당 유저가 작성한 해당 음식의 리뷰를 삭제")
    void testDeleteReview() throws Exception {
        // given
        User reviewUser = userService.findUser(userResDto.getUserId());
        Company company = createCompany("testCompany", "companyLogo");
        Food food = createFood(company, "foodName", "foodImage", Category.CHICKEN);
        createReview(reviewUser, food, "comment", Score.GOOD);

        String url = "http://localhost:"+port+"/api/users/"+reviewUser.getUserId()+"/menus/"+food.getFoodId();
        // when
        mvc.perform(MockMvcRequestBuilders
            .delete(url)
            .requestAttr("loginUser", userResDto)
        ).andExpect(MockMvcResultMatchers.status().isOk());
        // then
        assertThrows(
            NoSuchElementException.class,
            () -> reviewService.findByFoodIdAndUserId(food.getFoodId(), reviewUser.getUserId())
        );
    }



}
