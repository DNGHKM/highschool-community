package com.dnghkm.high_school_community.entity;

import com.dnghkm.high_school_community.dto.PostRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@ToString(exclude = {"school", "user"})
@Builder
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @Enumerated(EnumType.STRING)
    @NotNull
    private BoardType boardType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column
    private String title;

    @NotNull
    @Column(length = 1000)
    private String content;

    @NotNull
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @NotNull
    @Column(name = "number_comments")
    private int numberComments = 0;

    @NotNull
    private int vote = 0;

    @NotNull
    @Column(name = "deleted")
    private boolean deleted = false;

    public void updatePost(PostRequestDto postRequestDto){
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.updateDate = LocalDateTime.now();
    }

    public void deletePost(){
        this.deleted = true;
    }
    public void upvote(){
        this.vote++;
    }
    public void downVote(){
        this.vote--;
    }
    public void addComments(){
        this.numberComments++;
    }
    public void deleteComments(){this.numberComments--;}
}