package com.dnghkm.high_school_community.service;

import com.dnghkm.high_school_community.dto.CommentRequestDto;
import com.dnghkm.high_school_community.dto.CommentResponseDto;
import com.dnghkm.high_school_community.entity.BoardType;
import com.dnghkm.high_school_community.entity.Comment;
import com.dnghkm.high_school_community.entity.Post;
import com.dnghkm.high_school_community.entity.User;
import com.dnghkm.high_school_community.exception.SchoolNotMatchException;
import com.dnghkm.high_school_community.exception.UserNotMatchException;
import com.dnghkm.high_school_community.repository.CommentRepository;
import com.dnghkm.high_school_community.repository.PostRepository;
import com.dnghkm.high_school_community.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.dnghkm.high_school_community.entity.BoardType.SCHOOL;
import static com.dnghkm.high_school_community.entity.BoardType.SCHOOL_ANONYMOUS;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    //댓글 작성
    public CommentResponseDto write(Long postId, CommentRequestDto commentRequestDto, String username) {
        User findUser = findUser(username);
        Post findPost = findPost(postId);
        verifySchool(findPost, findUser);
        Comment comment = Comment.builder()
                .post(findPost)
                .user(findUser)
                .content(commentRequestDto.getContent())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .vote(0)
                .build();
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    //댓글 조회
    public List<CommentResponseDto> find(Long postId, String username) {
        User findUser = findUser(username);
        Post findPost = findPost(postId);
        verifySchool(findPost, findUser);
        List<Comment> findList = commentRepository.findAllByPostId(postId);
        return findList.stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }

    //댓글 수정
    public CommentResponseDto update(Long postId, Long commentId, CommentRequestDto commentRequestDto, String username) {
        User findUser = findUser(username);
        Post findPost = findPost(postId);
        verifySchool(findPost, findUser);
        if (!findUser.equals(findPost.getUser())) {
            throw new UserNotMatchException();
        }
        Comment comment = findComment(commentId);
        comment.updateComment(commentRequestDto);
        return new CommentResponseDto(comment);
    }

    //댓글 삭제
    public void delete(Long postId, Long commentId, String username) {
        User findUser = findUser(username);
        Post findPost = findPost(postId);
        verifySchool(findPost, findUser);
        if (!findUser.equals(findPost.getUser())) {
            throw new UserNotMatchException();
        }
        commentRepository.deleteById(commentId);
    }

    //추천
    public int upvote(Long postId, Long commentId, String username) {
        Post findPost = findPost(postId);
        User findUser = findUser(username);
        verifySchool(findPost, findUser);
        Comment findComment = findComment(commentId);
        findComment.upvote();
        return findComment.getVote();
    }

    //비추천
    public int downvote(Long postId, Long commentId, String username) {
        Post findPost = findPost(postId);
        User findUser = findUser(username);
        verifySchool(findPost, findUser);
        Comment findComment = findComment(commentId);
        findComment.downVote();
        return findComment.getVote();
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

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
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
