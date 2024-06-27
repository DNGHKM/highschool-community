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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }

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

    public List<User> getUnPermittedUsers(){
        return userRepository.findAllByPermitFalse();
    }

    public User permitUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));
        user.permitUser();
        return userRepository.save(user);
    }
}
