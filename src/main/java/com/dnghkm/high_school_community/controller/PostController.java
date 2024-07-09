package com.dnghkm.high_school_community.controller;

import com.dnghkm.high_school_community.dto.PostRequestDto;
import com.dnghkm.high_school_community.dto.PostResponseDto;
import com.dnghkm.high_school_community.entity.BoardType;
import com.dnghkm.high_school_community.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    /**
     * json example
     * {
     *     "title":"테스트제목",
     *     "content" : "테스트 내용입니다 123123",
     *     "boardType" : "GLOBAL"
     * }
     *  boardType = {GLOBAL, GLOBAL_ANONYMOUS, SCHOOL, SCHOOL_ANONYMOUS}
     */
    @PostMapping
    public ResponseEntity<PostResponseDto> writePost(@RequestBody PostRequestDto postRequestDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(postService.write(postRequestDto, username));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(postService.find(postId, username));
    }

    //학교 게시판 실명게시글 전체조회
    @GetMapping("/school")
    public ResponseEntity<List<PostResponseDto>> getAllSchoolPosts() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(postService.findAllSchool(username, BoardType.SCHOOL));
    }

    //학교 게시판 익명 게시글 전체조회
    @GetMapping("/school/anonymous")
    public ResponseEntity<List<PostResponseDto>> getSchoolAnonymousPosts() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(postService.findAllSchool(username, BoardType.SCHOOL_ANONYMOUS));
    }

    //모두의 게시판 실명게시글 전체조회
    @GetMapping("/global")
    public ResponseEntity<List<PostResponseDto>> getAllGlobalPosts() {
        return ResponseEntity.ok(postService.findAllGlobal(BoardType.GLOBAL));
    }

    //모두의 게시판 익명게시글 전체조회
    @GetMapping("/global/anonymous")
    public ResponseEntity<List<PostResponseDto>> getAllGlobalAnonymousPosts() {
        return ResponseEntity.ok(postService.findAllGlobal(BoardType.GLOBAL_ANONYMOUS));
    }

    //게시글의 게시판은 변경할 수 없다.
    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(postService.update(postId, postRequestDto, username));
    }

    //게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        postService.delete(postId, username);
        return ResponseEntity.noContent().build();
    }

    //추천
    @PostMapping("/{postId}/upvote")
    public ResponseEntity<Integer> upvotePost(@PathVariable Long postId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int vote = postService.upvote(postId, username);
        return ResponseEntity.ok(vote);
    }

    //비추천
    @PostMapping("/{postId}/downvote")
    public ResponseEntity<Integer> downvotePost(@PathVariable Long postId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int vote = postService.downvote(postId, username);
        return ResponseEntity.ok(vote);
    }
}
