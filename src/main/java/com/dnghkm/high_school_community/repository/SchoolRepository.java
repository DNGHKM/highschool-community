package com.dnghkm.high_school_community.repository;

import com.dnghkm.high_school_community.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {
    boolean existsByAdminCode(String adminCode);
    School findByAdminCode(String adminCode);
}