package com.dnghkm.high_school_community.service;

import com.dnghkm.high_school_community.dto.UserRegisterDto;
import com.dnghkm.high_school_community.entity.Role;
import com.dnghkm.high_school_community.entity.User;
import com.dnghkm.high_school_community.repository.SchoolRepository;
import com.dnghkm.high_school_community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(UserRegisterDto userDto) {
        String username = userDto.getUsername();
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username is already in use");
        }
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(userDto.getPassword()))
                .name(userDto.getName())
                .role(Role.TEMP)
                .phone(userDto.getPhone().replaceAll("-",""))
                .email(userDto.getEmail())
                .school(schoolRepository.findByAdminCode(userDto.getAdminCode()))
                .signInDate(LocalDateTime.now())
                .permit(false).build();

        userRepository.save(user);

        return user;
    }
}
