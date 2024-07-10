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
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    //단일 게시글 조회
    public PostResponseDto find(Long postId, String username) {
        Post findPost = findPost(postId);
        User findUser = findUser(username);
        verifySchool(findPost, findUser);
        return new PostResponseDto(findPost);
    }

    //게시글 검색
    public List<PostResponseDto> searchPosts(String keyword, BoardType boardType, String searchType, String username) {
        if (boardType.toString().contains("ANONYMOUS") && searchType.equals("author")) {
            throw new RuntimeException("익명 게시판은 작성자로 검색할 수 없습니다.");
        }
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException("유저를 찾을 수 없습니다.")
        );
        if (boardType == BoardType.SCHOOL || boardType == BoardType.SCHOOL_ANONYMOUS) {
            return searchBySchool(keyword, boardType, searchType, user);
        } else {
            return searchByGlobal(keyword, boardType, searchType);
        }
    }

    //게시글 검색 - 공통게시판
    private List<PostResponseDto> searchByGlobal(String keyword, BoardType boardType, String searchType) {
        List<Post> findPosts;
        switch (searchType.toLowerCase()) {
            case "title" -> findPosts = postRepository.findByTitle(keyword, boardType);
            case "content" -> findPosts = postRepository.findByContent(keyword, boardType);
            case "author" -> findPosts = postRepository.findByAuthor(keyword, boardType);
            default -> throw new IllegalArgumentException("잘못된 검색 유형입니다.");
        }
        return findPosts.stream().map(PostResponseDto::new).collect(Collectors.toList());
    }

    //게시글 검색 - 학교게시판
    private List<PostResponseDto> searchBySchool(String keyword, BoardType boardType, String searchType, User user) {
        List<Post> findPosts;
        switch (searchType.toLowerCase()) {
            case "title" -> findPosts = postRepository.findByTitleAndSchool(keyword, boardType, user.getSchool());
            case "content" -> findPosts = postRepository.findByContentAndSchool(keyword, boardType, user.getSchool());
            case "author" -> findPosts = postRepository.findByAuthorAndSchool(keyword, boardType, user.getSchool());
            default -> throw new IllegalArgumentException("잘못된 검색 유형입니다.");
        }
        return findPosts.stream().map(PostResponseDto::new).collect(Collectors.toList());
    }


    //모두의 게시판 전체조회
    public List<PostResponseDto> findAllGlobal(BoardType boardType) {
        List<Post> findList = postRepository.findAllByBoardType(boardType);
        return findList.stream().map(PostResponseDto::new).collect(Collectors.toList());
    }

    //학교 게시판 게시글 전체 조회
    public List<PostResponseDto> findAllSchool(String username, BoardType boardType) {
        User user = findUser(username);
        School findSchool = user.getSchool();
        List<Post> findList = postRepository.findAllBySchoolAndBoardType(findSchool, boardType);
        return findList.stream().map(PostResponseDto::new).collect(Collectors.toList());
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
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty() || post.get().isDeleted()) {
            throw new RuntimeException("게시글이 존재하지 않습니다.");
        }
        return post.get();
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
