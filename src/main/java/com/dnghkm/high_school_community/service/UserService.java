package com.dnghkm.high_school_community.service;

import com.dnghkm.high_school_community.dto.AuthDto;
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

    public User register(AuthDto.SignUp signUp) {
        String username = signUp.getUsername();
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username is already in use");
        }
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(signUp.getPassword()))
                .name(signUp.getName())
                .role(Role.ROLE_TEMP)
                .phone(signUp.getPhone().replaceAll("-",""))
                .email(signUp.getEmail())
                .school(schoolRepository.findByAdminCode(signUp.getAdminCode()))
                .signInDate(LocalDateTime.now())
                .permit(false).build();
        userRepository.save(user);
        return user;
    }

    public List<User> getUnPermittedUsers(){
        return userRepository.findAllByPermitFalse();
    }

    public User permit(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다. userId = " + userId));
        user.permitUser();
        return userRepository.save(user);
    }

    public void reject(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다. userId = " + userId));
        if(user.isPermit()){
            throw new RuntimeException("이미 가입 승인 된 유저입니다.");
        }
        userRepository.delete(user);
    }
}
