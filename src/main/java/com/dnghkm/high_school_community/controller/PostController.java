package com.dnghkm.high_school_community.controller;

import com.dnghkm.high_school_community.dto.PostRequestDto;
import com.dnghkm.high_school_community.dto.PostResponseDto;
import com.dnghkm.high_school_community.entity.BoardType;
import com.dnghkm.high_school_community.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    /**
     * 예시)
     * {
     * "title":"테스트제목",
     * "content" : "테스트 내용입니다 123123",
     * "boardType" : "GLOBAL"
     * }
     */
    @PostMapping
    public ResponseEntity<PostResponseDto> writePost(@RequestBody PostRequestDto postRequestDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(postService.write(postRequestDto, username));
    }

    //게시글 상세조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(postService.find(postId, username));
    }

    //게시글 리스트 조회
    // 예시) http://localhost:8080/post?boardType=GLOBAL&page=1
    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> getAllPosts(
            @RequestParam BoardType boardType,
            @PageableDefault(sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(postService.findAllPosts(username, boardType, pageable));
    }

    //게시글 검색
    // 예시) http://localhost:8080/post/search?keyword=테스트제목&boardType=GLOBAL&searchType=title
    // searchType -> 제목(title), 내용(content), 작성자(author) 중 하나
    @GetMapping("/search")
    public ResponseEntity<Page<PostResponseDto>> searchPosts(
            @RequestParam String keyword,
            @RequestParam BoardType boardType,
            @RequestParam String searchType,
            @PageableDefault(sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Page<PostResponseDto> posts = postService.searchPosts(keyword, boardType, searchType, username, pageable);
        return ResponseEntity.ok(posts);
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
