package com.dnghkm.highschool_community.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "school")
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 7)
    private String code;

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