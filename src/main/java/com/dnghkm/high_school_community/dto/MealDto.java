package com.dnghkm.high_school_community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
public class MealDto {
    private LocalDate mealDate;
    private String mealType; //조, 중, 석식
    private String menu;
}
