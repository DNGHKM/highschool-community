package com.dnghkm.high_school_community.entity;

import com.dnghkm.high_school_community.dto.CommentDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@ToString(exclude = {"post"})
@Builder
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String content;

    @NotNull
    @Column(name = "create_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @NotNull
    private int vote = 0;

    public void updateComment(CommentDto commentDto){
        this.content = commentDto.getContent();
        this.updateDate = LocalDateTime.now();
    }

    public void upvote(){
        this.vote++;
    }

    public void downVote(){
        this.vote--;
    }
}
