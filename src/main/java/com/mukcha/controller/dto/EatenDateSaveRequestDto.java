package com.mukcha.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class EatenDateSaveRequestDto {

    @Schema(description = "먹은 날짜 (yyyy-mm-dd)", example = "2000-01-30")
    private String eatenDate;

    @Builder
    public EatenDateSaveRequestDto(String eatenDate) {
        this.eatenDate = eatenDate;
    }

}
