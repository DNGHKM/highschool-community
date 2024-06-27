package com.dnghkm.high_school_community.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
public class UserRegisterDto {
    private String username;
    private String password;
    private String name;
    private String adminCode;
    private String phone;
    private String email;
    private MultipartFile verificationFile;
}
