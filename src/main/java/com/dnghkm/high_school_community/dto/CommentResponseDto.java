package com.dnghkm.high_school_community.dto;

import com.dnghkm.high_school_community.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {
    private Long postId;
    private String content;
    private String username;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private int vote;

    public CommentResponseDto(Comment comment) {
        this.postId = comment.getPost().getId();
        this.content = comment.getContent();
        this.username = comment.getUser().getUsername();
        this.createDate = comment.getCreatedDate();
        this.updateDate = comment.getUpdateDate();
        this.vote = comment.getVote();
    }
}
