package com.dnghkm.high_school_community.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

public class AuthDto {
    @Data
    @Builder
    public static class SignIn {
        private String username;
        private String password;
    }

    @Data
    @Builder
    public static class SignUp {
        private String username;
        private String password;
        private String name;
        private String adminCode;
        private String phone;
        private String email;
        private MultipartFile verificationFile;
    }
}
