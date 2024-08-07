package com.dnghkm.high_school_community.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@ToString(exclude = {"school"})
@Builder
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 20)
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    @Column(length = 20)
    private String name;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Role role = Role.ROLE_TEMP;
    @NotNull
    @Column(unique = true, length = 15)
    private String phone;
    @NotNull
    @Column(unique = true, length = 100)
    private String email;
    @ManyToOne
    @JoinColumn(name = "school_id", nullable = false)
    private School school;
    @NotNull
    @Column(name = "signin_date")
    private LocalDateTime signInDate;
    @NotNull
    private boolean permit;

    public void permitUser() {
        this.role = Role.ROLE_USER;
        this.permit = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}
