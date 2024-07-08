package com.dnghkm.high_school_community.repository;

import com.dnghkm.high_school_community.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    List<User> findAllByPermitFalse();
    Optional<User> findByUsername(String username);
}
