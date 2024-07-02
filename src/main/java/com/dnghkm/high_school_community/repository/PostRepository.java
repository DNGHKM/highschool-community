package com.dnghkm.high_school_community.repository;

import com.dnghkm.high_school_community.entity.BoardType;
import com.dnghkm.high_school_community.entity.Post;
import com.dnghkm.high_school_community.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByBoardType(BoardType boardType);
    Post findByIdAndBoardType(Long postId, BoardType boardType);
    List<Post> findAllBySchoolAndBoardType(School school, BoardType boardType);
}
