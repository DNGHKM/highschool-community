package com.dnghkm.highschool_community.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "username")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 20)
    private String name;
    @Column(unique = true, length = 20)
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Role role = Role.TEMP;
    @NotNull
    @Column(length = 15)
    private String phone;
    @NotNull
    @Column(length = 100)
    private String email;
    @ManyToOne
    @JoinColumn(name = "school_id", nullable = false)
    private School school;
    @NotNull
    @Column(name = "signin_date")
    private LocalDateTime signInDate = LocalDateTime.now();
    @NotNull
    private boolean permit = false;
}
