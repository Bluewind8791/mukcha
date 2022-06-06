package com.mukcha.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequestDto {

    @Schema(description = "회원의 닉네임", example = "홍길동")
    private String nickname;

    @Schema(description = "회원의 프로필 이미지 URL", example = "/profile/blank.png")
    private String profileImage;

    @Schema(description = "회원의 성별 (MALE/FEMALE)", example = "MALE")
    private String gender;

    @Schema(description = "회원의 생년 (yyyy)", example = "2000")
    private String birthYear;

    @Builder
    public UserUpdateRequestDto(String nickname, String profileImage, String gender, String birthYear) {
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.gender = gender;
        this.birthYear = birthYear;
    }

    @Override
    public String toString() {
        return ">>> UserUpdateRequestDto [birthYear=" + birthYear 
            + ", gender=" + gender + ", nickname=" + nickname
            + ", profileImage=" + profileImage + "]";
    }

}