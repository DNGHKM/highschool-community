package com.dnghkm.high_school_community.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PostDto {
    private String title;
    private String content;
}