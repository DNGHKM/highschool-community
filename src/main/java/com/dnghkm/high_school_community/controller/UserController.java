package com.dnghkm.high_school_community.controller;

import com.dnghkm.high_school_community.dto.UserRegisterDto;
import com.dnghkm.high_school_community.entity.User;
import com.dnghkm.high_school_community.service.UserService;
import com.dnghkm.high_school_community.service.VerificationFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final VerificationFileService verificationFileService;

    /**
     *  # form-data POST 요청
     *  유저는 가입 정보 기입과 동시에 파일을 업로드하여 가입요청함
     *  USER 등록, 업로드 한 파일 저장함
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(
            @RequestPart("user") UserRegisterDto userRegisterDto,
            @RequestPart("verificationFile") MultipartFile multipartFile) {
        userRegisterDto.setVerificationFile(multipartFile);
        User registeredUser = userService.register(userRegisterDto);
        verificationFileService.register(registeredUser, userRegisterDto);
        return ResponseEntity.ok(registeredUser);
    }
}