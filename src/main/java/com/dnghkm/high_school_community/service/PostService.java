package com.dnghkm.high_school_community.service;

import com.dnghkm.high_school_community.dto.PostDto;
import com.dnghkm.high_school_community.entity.BoardType;
import com.dnghkm.high_school_community.entity.Post;
import com.dnghkm.high_school_community.entity.School;
import com.dnghkm.high_school_community.entity.User;
import com.dnghkm.high_school_community.repository.PostRepository;
import com.dnghkm.high_school_community.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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

    //게시글 작성
    public Post write(PostDto postDto, String username) {
        User user = userRepository.findByUsername(username);
        Post post = Post.builder()
                .school(user.getSchool())
                .boardType(postDto.getBoardType())
                .user(user)
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now()).build();
        postRepository.save(post);
        return post;
    }

    //게시글 수정
    public Post update(Long PostId, PostDto postDto, String username) {
        Post findPost = postRepository.findById(PostId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        User user = userRepository.findByUsername(username);
        if (!findPost.getUser().equals(user)) {
            throw new EntityNotFoundException("유저가 불일치합니다.");
        }
        findPost.updatePost(postDto);
        return findPost;
    }

    //게시글 삭제
    public void delete(Long PostId, String username) {
        Post findPost = postRepository.findById(PostId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        User user = userRepository.findByUsername(username);
        if (!findPost.getUser().equals(user)) {
            throw new EntityNotFoundException("유저가 불일치합니다.");
        }
        findPost.deletePost();
    }

    //공통 게시판 전체조회
    public List<Post> getAllGlobalPosts(BoardType boardType) {
        return postRepository.findAllByBoardTypeAndDeletedIsFalse(boardType);
    }

    //공통 게시판 게시글 단일 상세조회
    public Post getGlobalPost(Long postId, BoardType boardType) {
        return postRepository.findByIdAndBoardTypeAndDeletedIsFalse(postId, boardType);
    }

    //학교 게시판 게시글 전체 조회
    public List<Post> getAllSchoolPosts(String username, BoardType boardType) {
        User user = userRepository.findByUsername(username);
        School findSchool = user.getSchool();
        return postRepository.findAllBySchoolAndBoardTypeAndDeletedIsFalse(findSchool, boardType);
    }

    //공통 게시판 게시글 단일 상세조회
    public Post getSchoolPost(String username, Long postId, BoardType boardType) {
        School findSchool = userRepository.findByUsername(username).getSchool();
        Post post = postRepository.findByIdAndBoardTypeAndDeletedIsFalse(postId, boardType);
        if (!post.getSchool().equals(findSchool)) {
            throw new RuntimeException("게시글 조회 권한이 없습니다.");
        }
        return post;
    }
}
