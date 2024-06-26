package com.dnghkm.high_school_community.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@ToString(exclude = {"school"})
@Builder
@Table(name = "meal")
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @Column(name = "meal_date")
    private LocalDate mealDate;

    @NotNull
    @Column(name = "meal_type", length = 20)
    private String mealType; //조, 중, 석식

    @NotNull
    @Column(length = 1000)
    private String menu;
}
