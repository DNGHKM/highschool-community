package com.dnghkm.high_school_community.service;

import com.dnghkm.high_school_community.dto.PostDto;
import com.dnghkm.high_school_community.entity.BoardType;
import com.dnghkm.high_school_community.entity.Post;
import com.dnghkm.high_school_community.entity.School;
import com.dnghkm.high_school_community.entity.User;
import com.dnghkm.high_school_community.repository.PostRepository;
import com.dnghkm.high_school_community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * Todo : Paging 구현
     */

    //공통 게시판 전체조회
    public List<Post> getAllGlobalPosts(BoardType boardType) {
        return postRepository.findAllByBoardType(boardType);
    }

    //공통 게시판 게시글 단일 상세조회
    public Post getGlobalPost(Long postId, BoardType boardType) {
        return postRepository.findByIdAndBoardType(postId, boardType);
    }

    //학교 게시판 게시글 전체 조회
    public List<Post> getAllSchoolPosts(String username, BoardType boardType) {
        User user = userRepository.findByUsername(username);
        School findSchool = user.getSchool();
        return postRepository.findAllBySchoolAndBoardType(findSchool, boardType);
    }

    //공통 게시판 게시글 단일 상세조회
    public Post getSchoolPost(String username, Long postId, BoardType boardType) {
        School findSchool = userRepository.findByUsername(username).getSchool();
        Post post = postRepository.findByIdAndBoardType(postId, boardType);
        if (!post.getSchool().equals(findSchool)) {
            throw new RuntimeException("게시글 조회 권한이 없습니다.");
        }
        return post;
    }

    //게시글 작성
    public Post writePost(PostDto postDto, String username, BoardType boardType) {
        User user = userRepository.findByUsername(username);
        Post post = Post.builder()
                .school(user.getSchool())
                .boardType(boardType)
                .user(user)
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now()).build();
        postRepository.save(post);
        return post;
    }
}
