package com.dnghkm.high_school_community.service;

import com.dnghkm.high_school_community.dto.PostRequestDto;
import com.dnghkm.high_school_community.dto.PostResponseDto;
import com.dnghkm.high_school_community.entity.BoardType;
import com.dnghkm.high_school_community.entity.Post;
import com.dnghkm.high_school_community.entity.School;
import com.dnghkm.high_school_community.entity.User;
import com.dnghkm.high_school_community.exception.SchoolNotMatchException;
import com.dnghkm.high_school_community.exception.UserNotMatchException;
import com.dnghkm.high_school_community.repository.PostRepository;
import com.dnghkm.high_school_community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    static final String SCHOOL = "SCHOOL";

    /**
     * Todo : Paging 구현
     */

    //게시글 작성
    public PostResponseDto write(PostRequestDto postRequestDto, String username) {
        User user = findUser(username);
        Post post = Post.builder()
                .school(user.getSchool())
                .boardType(postRequestDto.getBoardType())
                .user(user)
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now()).build();
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    //게시글 리스트 조회
    public Page<PostResponseDto> findAllPosts(String username, BoardType boardType, Pageable pageable) {
        User user = findUser(username);
        Page<Post> findPage;

        if (boardType.toString().contains(SCHOOL)) {
            findPage = postRepository.findAllBySchoolAndBoardType(user.getSchool(), boardType, pageable);
        } else {
            findPage = postRepository.findAllByBoardType(boardType, pageable);
        }

        List<PostResponseDto> responseDtos = findPage.stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

        return new PageImpl<>(responseDtos, pageable, findPage.getTotalElements());
    }

    //단일 게시글 조회
    public PostResponseDto find(Long postId, String username) {
        Post findPost = findPost(postId);
        User findUser = findUser(username);
        verifySchool(findPost, findUser);
        return new PostResponseDto(findPost);
    }

    //게시글 검색
    public Page<PostResponseDto> searchPosts(
            String keyword, BoardType boardType, String searchType, String username, Pageable pageable) {
        if (boardType.toString().contains("ANONYMOUS") && searchType.equals("author")) {
            throw new RuntimeException("익명 게시판은 작성자로 검색할 수 없습니다.");
        }
        User user = findUser(username);

        if (boardType.toString().contains(SCHOOL)) {
            return searchBySchool(keyword, boardType, searchType, user, pageable);
        } else {
            return searchByGlobal(keyword, boardType, searchType, pageable);
        }
    }

    //게시글 검색 - 공통게시판
    private Page<PostResponseDto> searchByGlobal(String keyword, BoardType boardType, String searchType, Pageable pageable) {
        Page<Post> findPage;
        switch (searchType.toLowerCase()) {
            case "title" -> findPage = postRepository.findByTitle(keyword, boardType, pageable);
            case "content" -> findPage = postRepository.findByContent(keyword, boardType, pageable);
            case "author" -> findPage = postRepository.findByAuthor(keyword, boardType, pageable);
            default -> throw new IllegalArgumentException("잘못된 검색 유형입니다.");
        }
        List<PostResponseDto> responseDtos = findPage.stream().map(PostResponseDto::new).collect(Collectors.toList());
        return new PageImpl<>(responseDtos, pageable, findPage.getTotalElements());
    }

    //게시글 검색 - 학교게시판
    private Page<PostResponseDto> searchBySchool(String keyword, BoardType boardType, String searchType, User user, Pageable pageable) {
        Page<Post> findPage;
        switch (searchType.toLowerCase()) {
            case "title" -> findPage = postRepository.findByTitleAndSchool(keyword, boardType, user.getSchool(), pageable);
            case "content" -> findPage = postRepository.findByContentAndSchool(keyword, boardType, user.getSchool(), pageable);
            case "author" -> findPage = postRepository.findByAuthorAndSchool(keyword, boardType, user.getSchool(), pageable);
            default -> throw new IllegalArgumentException("잘못된 검색 유형입니다.");
        }
        List<PostResponseDto> responseDtos = findPage.stream().map(PostResponseDto::new).collect(Collectors.toList());
        return new PageImpl<>(responseDtos, pageable, findPage.getTotalElements());
    }


    //게시글 수정
    public PostResponseDto update(Long postId, PostRequestDto postRequestDto, String username) {
        Post findPost = findPost(postId);
        User user = findUser(username);
        if (!findPost.getUser().equals(user)) {
            throw new UserNotMatchException();
        }
        findPost.updatePost(postRequestDto);
        return new PostResponseDto(findPost);
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
        if (boardType.toString().contains(SCHOOL)) {
            if (!findPost.getSchool().equals(school)) {
                throw new SchoolNotMatchException();
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
        if (boardType.toString().contains(SCHOOL)) {
            if (!findPost.getSchool().equals(school)) {
                throw new SchoolNotMatchException();
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
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty() || post.get().isDeleted()) {
            throw new RuntimeException("게시글이 존재하지 않습니다.");
        }
        return post.get();
    }

    private static void verifySchool(Post findPost, User user) {
        BoardType boardType = findPost.getBoardType();
        if (boardType.toString().contains(SCHOOL)) {
            if (!user.getSchool().equals(findPost.getSchool())) {
                throw new SchoolNotMatchException();
            }
        }
    }

}
