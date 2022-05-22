package com.mukcha.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class EatenDateSaveRequestDto {

    private String eatenDate;

    @Builder
    public EatenDateSaveRequestDto(String eatenDate) {
        this.eatenDate = eatenDate;
    }

}
