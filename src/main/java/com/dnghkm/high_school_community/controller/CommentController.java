package com.dnghkm.high_school_community.controller;

import com.dnghkm.high_school_community.dto.CommentRequestDto;
import com.dnghkm.high_school_community.dto.CommentResponseDto;
import com.dnghkm.high_school_community.service.CommentService;
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
@RequestMapping("/post/{postId}/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<Page<CommentResponseDto>> getComments(
            @PathVariable Long postId,
            @PageableDefault(sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(commentService.find(postId, username, pageable));
    }

    @PostMapping
    public ResponseEntity<CommentResponseDto> writeComment(@PathVariable Long postId, @RequestBody CommentRequestDto commentRequestDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(commentService.write(postId, commentRequestDto, username));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(commentService.update(postId, commentId, commentRequestDto, username));
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
