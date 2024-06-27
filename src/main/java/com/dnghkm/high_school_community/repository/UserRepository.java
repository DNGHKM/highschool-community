package com.dnghkm.high_school_community.repository;

import com.dnghkm.high_school_community.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
}
