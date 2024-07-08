package com.dnghkm.high_school_community.controller;

import com.dnghkm.high_school_community.dto.CommentDto;
import com.dnghkm.high_school_community.entity.Comment;
import com.dnghkm.high_school_community.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/post/{postId}/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long postId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(commentService.find(postId, username));
    }

    @PostMapping
    public ResponseEntity<Comment> writeComment(@PathVariable Long postId, @RequestBody CommentDto commentDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(commentService.write(postId, commentDto, username));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentDto commentDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(commentService.update(postId, commentId, commentDto, username));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        commentService.delete(postId, commentId, username);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{commentId}/upvote")
    public ResponseEntity<Integer> upvoteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int vote = commentService.upvote(postId, commentId, username);
        return ResponseEntity.ok(vote);
    }

    @PostMapping("/{commentId}/downvote")
    public ResponseEntity<Integer> downvoteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int vote = commentService.downvote(postId, commentId, username);
        return ResponseEntity.ok(vote);
    }
}
