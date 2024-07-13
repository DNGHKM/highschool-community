package com.dnghkm.high_school_community.repository;

import com.dnghkm.high_school_community.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByPostIdOrderByCreateDateDesc(Long postId, Pageable pageable);
}
