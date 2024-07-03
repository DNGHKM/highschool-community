package com.dnghkm.high_school_community.controller;

import com.dnghkm.high_school_community.entity.User;
import com.dnghkm.high_school_community.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //ADMIN 권한자만 - 회원 목록 조회
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    //ADMIN 권한자만 사용 - 미 허가 회원 목록 조회
    @GetMapping("/permit")
    public ResponseEntity<List<User>> getUnPermittedUsers() {
        return ResponseEntity.ok(userService.getUnPermittedUsers());
    }

    //ADMIN 권한자만 사용 - 회원 정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PatchMapping("/permit/{userId}")
    public ResponseEntity<User> permitUser(@PathVariable Long userId) {
        User user = userService.permitUser(userId);
        return ResponseEntity.ok(user);
    }
}