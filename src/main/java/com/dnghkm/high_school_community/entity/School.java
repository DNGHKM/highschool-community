package com.dnghkm.high_school_community.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@ToString
@Builder
@Table(name = "school")
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "admin_code", length = 7, unique = true)
    private String adminCode;

    @NotNull
    @Column(name = "sido_code", length = 20)
    private String sidoCode;

    @NotNull
    @Column(length = 50)
    private String name;

    @NotNull
    @Column(name = "school_type", length = 4)
    private String schoolType; //초,중,고등학교

    @NotNull
    @Column(length = 100)
    private String address;

    @Column(name = "address_detail", length = 100)
    private String addressDetail;
}