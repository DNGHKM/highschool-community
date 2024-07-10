package com.dnghkm.high_school_community.dto;

import com.dnghkm.high_school_community.entity.BoardType;
import com.dnghkm.high_school_community.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private BoardType boardType;
    private String schoolName;
    private String username;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private int numberComments;
    private int vote;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.boardType = post.getBoardType();
        this.schoolName = post.getSchool().getName();
        this.username = post.getUser().getUsername();
        this.createDate = post.getCreateDate();
        this.updateDate = post.getUpdateDate();
        this.numberComments = post.getNumberComments();
        this.vote = post.getVote();
    }
}