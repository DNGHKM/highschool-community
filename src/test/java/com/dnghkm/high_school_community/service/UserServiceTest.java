package com.dnghkm.high_school_community.service;

import com.dnghkm.high_school_community.dto.AuthDto;
import com.dnghkm.high_school_community.entity.Role;
import com.dnghkm.high_school_community.entity.School;
import com.dnghkm.high_school_community.entity.User;
import com.dnghkm.high_school_community.repository.SchoolRepository;
import com.dnghkm.high_school_community.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SchoolRepository schoolRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("정상회원가입")
    void testRegister(){
        //given
        String username = "testUser";
        String encodedPassword = "encodedPassword";
        String adminCode = "12345";
        School school = School.builder().adminCode(adminCode).build();

        AuthDto.SignUp signUp = AuthDto.SignUp.builder()
                .username("testUser")
                .password("abcd")
                .name("Test User")
                .adminCode(adminCode)
                .phone("010-1234-1234")
                .email("abcd@gamil.com").build();

        given(userRepository.existsByUsername(username)).willReturn(false);
        given(passwordEncoder.encode(signUp.getPassword())).willReturn(encodedPassword);
        given(schoolRepository.findByAdminCode(adminCode)).willReturn(school);

        //when
        User user = userService.register(signUp);

        //then
        assertNotNull(user);
        assertEquals(signUp.getUsername(), user.getUsername());
        assertEquals(encodedPassword, user.getPassword());
        assertEquals(username, user.getUsername());
        assertEquals(school, user.getSchool());
        assertFalse(user.isPermit());
        assertNotNull(user.getSignInDate());

        then(userRepository).should(times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("이미 존재하는 회원명_가입불가")
    void testRegister_UsernameAlreadyExists(){
        //given
        String username = "testUser";
        AuthDto.SignUp signUp = AuthDto.SignUp.builder().username(username).build();

        given(userRepository.existsByUsername(username)).willReturn(true);
        //when & then
        assertThrows(RuntimeException.class,
                () -> userService.register(signUp));
    }

    @Test
    @DisplayName("유저 허가")
    void permit(){
        //given
        User user = User.builder().id(1L).permit(false).role(Role.ROLE_TEMP).build();

        given(userRepository.findById(1L)).willReturn(java.util.Optional.of(user));
        given(userRepository.save(any(User.class))).willAnswer(invocation -> invocation.getArgument(0));

        //when
        userService.permit(1L);

        //then
        assertTrue(user.isPermit());
        assertEquals(user.getRole(), Role.ROLE_USER);
    }
}