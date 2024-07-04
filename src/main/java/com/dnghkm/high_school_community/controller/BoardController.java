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
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final PostService postService;

    /**
     * 공통 게시판
     */
    @PostMapping("/global")
    public ResponseEntity<Post> postGlobal(@RequestBody PostDto postDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(postService.writePost(postDto, username, BoardType.GLOBAL));
    }

    @PostMapping("/global/anonymous")
    public ResponseEntity<Post> postGlobalAnonymous(@RequestBody PostDto postDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(postService.writePost(postDto, username, BoardType.GLOBAL_ANONYMOUS));
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
    @PostMapping("/school")
    public ResponseEntity<Post> postSchool(@RequestBody PostDto postDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(postService.writePost(postDto, username, BoardType.SCHOOL));
    }

    @PostMapping("/school/anonymous")
    public ResponseEntity<Post> postSchoolAnonymous(@RequestBody PostDto postDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(postService.writePost(postDto, username, BoardType.SCHOOL_ANONYMOUS));
    }

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
