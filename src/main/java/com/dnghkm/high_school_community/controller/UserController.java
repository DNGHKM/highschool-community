package com.dnghkm.high_school_community.controller;

import com.dnghkm.high_school_community.entity.User;
import com.dnghkm.high_school_community.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUser(pageable));
    }

    @GetMapping("/permit")
    public ResponseEntity<List<User>> getUnPermittedUsers() {
        return ResponseEntity.ok(userService.getUnPermittedUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PatchMapping("/permit/{userId}")
    public ResponseEntity<User> permitUser(@PathVariable Long userId) {
        User user = userService.permit(userId);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/reject/{userId}")
    public ResponseEntity<Void> rejectUser(@PathVariable Long userId) {
        userService.reject(userId);
        return  ResponseEntity.noContent().build();
    }
}