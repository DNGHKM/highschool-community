package com.dnghkm.high_school_community.config;

import com.dnghkm.high_school_community.auth.JwtAuthorizationFilter;
import com.dnghkm.high_school_community.auth.JwtFilter;
import com.dnghkm.high_school_community.auth.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration configuration;
    private final JwtUtil jwtUtil;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth", "/schools", "/").permitAll()
                        .requestMatchers("/post/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/comment/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/meal/**").hasAnyRole("USER", "ADMIN")
//                        .requestMatchers("/user/**").hasRole("ADMIN")
                        .anyRequest().permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtFilter(jwtUtil), JwtAuthorizationFilter.class)
                .addFilterAt(new JwtAuthorizationFilter(authenticationManager(configuration), jwtUtil), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
