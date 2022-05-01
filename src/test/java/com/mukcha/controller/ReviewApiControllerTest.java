package com.mukcha.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mukcha.controller.dto.ReviewSaveRequestDto;
import com.mukcha.controller.helper.WithControllerTest;
import com.mukcha.domain.Category;
import com.mukcha.domain.Company;
import com.mukcha.domain.Food;
import com.mukcha.domain.Review;
import com.mukcha.domain.Score;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReviewApiControllerTest extends WithControllerTest {

    String url = "http://localhost:" + port + "/api/reviews";

    @BeforeEach
    void before() {
        restTemplate.getRestTemplate().setRequestFactory(
            new HttpComponentsClientHttpRequestFactory()
        );
    }

    @Test
    @DisplayName("리뷰의 코멘트와 점수를 저장한다.")
    void testSaveReview() {
        // given
        Company company = createCompany("testCompany", "companyLogo");
        Food food = createFood(company, "foodName", "foodImage", Category.CHICKEN);
        createUser("test@email.com", "nickname");
        ReviewSaveRequestDto requestDto = ReviewSaveRequestDto.builder()
            .comment("comment")
            .rating("또 먹고 싶어요")
            .build()
        ;
        url += "/"+food.getFoodId();
        // when
        // ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestDto, String.class);
        ResponseEntity<String> response = restTemplate.withBasicAuth("test@email.com", "123123")
            .postForEntity(url, requestDto, String.class);
        // then
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        Review savedReview = reviewRepository.findAll().get(0);
        assertEquals("comment", savedReview.getComment());
        assertEquals(Score.GOOD, savedReview.getScore());
        assertEquals("foodName", savedReview.getFood().getName());
        assertEquals("nickname", savedReview.getUser().getNickname());
    }


}
