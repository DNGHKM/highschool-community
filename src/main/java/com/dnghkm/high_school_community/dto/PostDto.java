package com.dnghkm.high_school_community.dto;

import com.dnghkm.high_school_community.entity.BoardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class PostDto {
    private String title;
    private String content;
    private BoardType boardType;
}