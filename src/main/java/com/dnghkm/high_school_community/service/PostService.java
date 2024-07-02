package com.dnghkm.high_school_community.service;

import com.dnghkm.high_school_community.entity.BoardType;
import com.dnghkm.high_school_community.entity.Post;
import com.dnghkm.high_school_community.entity.School;
import com.dnghkm.high_school_community.entity.User;
import com.dnghkm.high_school_community.repository.PostRepository;
import com.dnghkm.high_school_community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * Todo : Paging 구현
     */
    //공통게시판 게시글 전체 조회
    public List<Post> getAllGlobalPost() {
        return postRepository.findAllByBoardType(BoardType.GLOBAL);
    }

    //공통 게시글 단일조회
    public Post getGlobalPost(Long postId) {
        return postRepository.findByIdAndBoardType(postId, BoardType.GLOBAL);
    }

    //공통-익명게시판 게시글 전체 조회
    public List<Post> getAllGlobalAnonymousPosts() {
        return postRepository.findAllByBoardType(BoardType.GLOBAL_ANONYMOUS);
    }

    //공통-익명 게시글 단일조회
    public Post getGlobalAnonymousPost(Long postId) {
        return postRepository.findByIdAndBoardType(postId, BoardType.GLOBAL_ANONYMOUS);
    }

    //학교 게시판 게시글 전체 조회
    public List<Post> getAllSchoolPost(String username) {
        User user = userRepository.findByUsername(username);
        School findSchool = user.getSchool();
        return postRepository.findAllBySchoolAndBoardType(findSchool, BoardType.SCHOOL);
    }

    //학교게시판 게시글 단일조회
    public Post getSchoolPost(String username, Long postId) {
        School findSchool = userRepository.findByUsername(username).getSchool();
        Post post = postRepository.findByIdAndBoardType(postId, BoardType.SCHOOL);
        if (!post.getSchool().equals(findSchool)) {
            throw new RuntimeException("게시글 조회 권한이 없습니다.");
        }
        return post;
    }

    //학교-익명게시판 게시글 전체 조회
    public List<Post> getAllSchoolAnonymousPost(String username) {
        School findSchool = userRepository.findByUsername(username).getSchool();
        return postRepository.findAllBySchoolAndBoardType(findSchool, BoardType.SCHOOL_ANONYMOUS);
    }

    //학교-익명게시판 게시글 단일조회
    public Post getSchoolAnonymousPost(String username, Long postId) {
        School findSchool = userRepository.findByUsername(username).getSchool();
        Post post = postRepository.findByIdAndBoardType(postId, BoardType.SCHOOL_ANONYMOUS);
        if (!post.getSchool().equals(findSchool)) {
            throw new RuntimeException("게시글 조회 권한이 없습니다.");
        }
        return post;
    }
}
