package com.dnghkm.high_school_community.controller;

import com.dnghkm.high_school_community.dto.PostDto;
import com.dnghkm.high_school_community.entity.BoardType;
import com.dnghkm.high_school_community.entity.Post;
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

    //게시글의 게시판은 변경할 수 없다.
    @PostMapping
    public ResponseEntity<Post> writePost(@RequestBody PostDto postDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(postService.write(postDto, username));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Post> updatePost(@PathVariable Long postId, @RequestBody PostDto postDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(postService.update(postId, postDto, username));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        postService.delete(postId, username);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upvote/{postId}")
    public ResponseEntity<Integer> upvotePost(@PathVariable Long postId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int vote = postService.upvote(postId, username);
        return ResponseEntity.ok(vote);
    }

    @PostMapping("/downvote/{postId}")
    public ResponseEntity<Integer> downvotePost(@PathVariable Long postId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int vote = postService.downvote(postId, username);
        return ResponseEntity.ok(vote);
    }

    @GetMapping("/global")
    public ResponseEntity<List<Post>> getAllGlobalPosts() {
        return ResponseEntity.ok(postService.getAllGlobalPosts(BoardType.GLOBAL));
    }

    @GetMapping("/global/{postId}")
    public ResponseEntity<Post> getGlobalPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getGlobalPost(postId, BoardType.GLOBAL));
    }

    @GetMapping("/global/anonymous")
    public ResponseEntity<List<Post>> getAllGlobalAnonymousPosts() {
        return ResponseEntity.ok(postService.getAllGlobalPosts(BoardType.GLOBAL_ANONYMOUS));
    }

    @GetMapping("/global/anonymous/{postId}")
    public ResponseEntity<Post> getGlobalAnonymousPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getGlobalPost(postId, BoardType.GLOBAL_ANONYMOUS));
    }

    /**
     * 학교 게시판
     */

    @GetMapping("/school")
    public ResponseEntity<List<Post>> getAllSchoolPosts() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(postService.getAllSchoolPosts(username, BoardType.SCHOOL));
    }

    @GetMapping("/school/anonymous")
    public ResponseEntity<List<Post>> getSchoolAnonymousPosts() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(postService.getAllSchoolPosts(username, BoardType.SCHOOL_ANONYMOUS));
    }

    @GetMapping("/school/{postId}")
    public ResponseEntity<Post> getSchoolPost(@PathVariable Long postId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(postService.getSchoolPost(username, postId, BoardType.SCHOOL));
    }

    @GetMapping("/school/anonymous/{postId}")
    public ResponseEntity<Post> getSchoolAnonymousPost(@PathVariable Long postId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(postService.getSchoolPost(username, postId, BoardType.SCHOOL_ANONYMOUS));
    }
}
