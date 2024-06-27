package com.dnghkm.high_school_community.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SchoolDto {
    private String adminCode;
    private String sidoCode;
    private String name;
    private String schoolType; //초,중,고등학교
    private String address;
    private String addressDetail;
}
