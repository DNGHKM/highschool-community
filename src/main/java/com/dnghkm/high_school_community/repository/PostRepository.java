package com.dnghkm.high_school_community.repository;

import com.dnghkm.high_school_community.entity.BoardType;
import com.dnghkm.high_school_community.entity.Post;
import com.dnghkm.high_school_community.entity.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.boardType = :boardType AND p.deleted = false ORDER BY p.createDate DESC")
    Page<Post> findAllByBoardType(
            @Param("boardType") BoardType boardType, Pageable pageable
    );

    @Query("SELECT p FROM Post p WHERE p.school = :school AND p.boardType = :boardType AND p.deleted = false ORDER BY p.createDate DESC")
    Page<Post> findAllBySchoolAndBoardType(
            @Param("school") School school, @Param("boardType") BoardType boardType, Pageable pageable
    );

    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% AND p.boardType = :boardType AND p.deleted = false ORDER BY p.createDate DESC")
    Page<Post> findByTitle(
            @Param("keyword") String keyword, @Param("boardType") BoardType boardType, Pageable pageable
    );

    @Query("SELECT p FROM Post p WHERE p.content LIKE %:keyword% AND p.boardType = :boardType AND p.deleted = false ORDER BY p.createDate DESC")
    Page<Post> findByContent(
            @Param("keyword") String keyword, @Param("boardType") BoardType boardType, Pageable pageable
    );

    @Query("SELECT p FROM Post p WHERE p.user.name LIKE %:keyword% AND p.boardType = :boardType AND p.deleted = false ORDER BY p.createDate DESC")
    Page<Post> findByAuthor(
            @Param("keyword") String keyword, @Param("boardType") BoardType boardType, Pageable pageable
    );

    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% AND p.boardType = :boardType AND p.school = :school AND p.deleted = false ORDER BY p.createDate DESC")
    Page<Post> findByTitleAndSchool(
            @Param("keyword") String keyword, @Param("boardType") BoardType boardType, @Param("school") School school, Pageable pageable
    );

    @Query("SELECT p FROM Post p WHERE p.content LIKE %:keyword% AND p.boardType = :boardType AND p.school = :school AND p.deleted = false ORDER BY p.createDate DESC")
    Page<Post> findByContentAndSchool(
            @Param("keyword") String keyword, @Param("boardType") BoardType boardType, @Param("school") School school, Pageable pageable
    );

    @Query("SELECT p FROM Post p WHERE p.user.name LIKE %:keyword% AND p.boardType = :boardType AND p.school = :school AND p.deleted = false ORDER BY p.createDate DESC")
    Page<Post> findByAuthorAndSchool(
            @Param("keyword") String keyword, @Param("boardType") BoardType boardType, @Param("school") School school, Pageable pageable
    );

}