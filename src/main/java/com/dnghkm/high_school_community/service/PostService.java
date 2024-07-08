package com.dnghkm.high_school_community.service;

import com.dnghkm.high_school_community.dto.PostDto;
import com.dnghkm.high_school_community.entity.BoardType;
import com.dnghkm.high_school_community.entity.Post;
import com.dnghkm.high_school_community.entity.School;
import com.dnghkm.high_school_community.entity.User;
import com.dnghkm.high_school_community.exception.SchoolNotMatchException;
import com.dnghkm.high_school_community.exception.UserNotMatchException;
import com.dnghkm.high_school_community.repository.PostRepository;
import com.dnghkm.high_school_community.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.dnghkm.high_school_community.entity.BoardType.SCHOOL;
import static com.dnghkm.high_school_community.entity.BoardType.SCHOOL_ANONYMOUS;

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
        User user = findUser(username);
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

    //단일 게시글 조회
    public Post find(Long postId, String username) {
        Post findPost = findPost(postId);
        User findUser = findUser(username);
        verifySchool(findPost, findUser);
        return findPost;
    }

    //모두의 게시판 전체조회
    public List<Post> findAllGlobal(BoardType boardType) {
        return postRepository.findAllByBoardTypeAndDeletedIsFalse(boardType);
    }

    //학교 게시판 게시글 전체 조회
    public List<Post> findAllSchool(String username, BoardType boardType) {
        User user = findUser(username);
        School findSchool = user.getSchool();
        return postRepository.findAllBySchoolAndBoardTypeAndDeletedIsFalse(findSchool, boardType);
    }

    //게시글 수정
    public Post update(Long postId, PostDto postDto, String username) {
        Post findPost = findPost(postId);
        User user = findUser(username);
        if (!findPost.getUser().equals(user)) {
            throw new UserNotMatchException();
        }
        findPost.updatePost(postDto);
        return findPost;
    }

    //게시글 삭제
    public void delete(Long postId, String username) {
        Post findPost = findPost(postId);
        User user = findUser(username);
        if (!findPost.getUser().equals(user)) {
            throw new UserNotMatchException();
        }
        findPost.deletePost();
    }

    public int upvote(Long postId, String username) {
        Post findPost = findPost(postId);
        BoardType boardType = findPost.getBoardType();
        User user = findUser(username);
        School school = user.getSchool();
        if (boardType == SCHOOL || boardType == SCHOOL_ANONYMOUS) {
            if (!findPost.getSchool().equals(school)) {
                throw new RuntimeException("게시글 투표 권한이 없습니다.");
            }
        }
        findPost.upvote();
        return findPost.getVote();
    }

    public int downvote(Long postId, String username) {
        Post findPost = findPost(postId);
        BoardType boardType = findPost.getBoardType();
        User user = findUser(username);
        School school = user.getSchool();
        if (boardType == SCHOOL || boardType == SCHOOL_ANONYMOUS) {
            if (!findPost.getSchool().equals(school)) {
                throw new RuntimeException("게시글 투표 권한이 없습니다.");
            }
        }
        findPost.downVote();
        return findPost.getVote();
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("유저를 찾을 수 없습니다. username = " + username)
        );
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
    }

    private static void verifySchool(Post findPost, User user) {
        BoardType boardType = findPost.getBoardType();
        if (boardType == SCHOOL || boardType == SCHOOL_ANONYMOUS) {
            if (!user.getSchool().equals(findPost.getSchool())) {
                throw new SchoolNotMatchException();
            }
        }
    }
}
