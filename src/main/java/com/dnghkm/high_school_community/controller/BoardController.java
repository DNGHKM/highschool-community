package com.dnghkm.high_school_community.controller;

import com.dnghkm.high_school_community.entity.Post;
import com.dnghkm.high_school_community.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final PostService postService;

    @GetMapping("/global")
    public ResponseEntity<List<Post>> getAllGlobalPosts() {
        return ResponseEntity.ok(postService.getAllGlobalPost());
    }

    @GetMapping("/global/{postId}")
    public ResponseEntity<Post> getGlobalPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getGlobalPost(postId));
    }

    @GetMapping("/global/anonymous")
    public ResponseEntity<List<Post>> getAllGlobalAnonymousPosts() {
        return ResponseEntity.ok(postService.getAllGlobalAnonymousPosts());
    }

    @GetMapping("/global/anonymous/{postId}")
    public ResponseEntity<Post> getGlobalAnonymousPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getGlobalAnonymousPost(postId));
    }

    @GetMapping("/school")
    public ResponseEntity<List<Post>> getAllSchoolPosts() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(postService.getAllSchoolPost(username));
    }

    @GetMapping("/school/{postId}")
    public ResponseEntity<Post> getSchoolPost(@PathVariable Long postId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(postService.getSchoolPost(username, postId));

    }

    @GetMapping("/school/anonymous")
    public ResponseEntity<List<Post>> getSchoolAnonymousPosts() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(postService.getAllSchoolAnonymousPost(username));
    }

    @GetMapping("/school/anonymous/{postId}")
    public ResponseEntity<Post> getSchoolAnonymousPost(@PathVariable Long postId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(postService.getSchoolAnonymousPost(username, postId));
    }
}
