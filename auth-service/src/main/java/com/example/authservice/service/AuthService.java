package com.example.authservice.service;

import com.example.authservice.dto.*;
import com.example.authservice.model.User;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.security.JwtUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.Set;

/**
 * Service class containing business logic for authentication and user management.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostConstruct
    public void createDefaultAdmin() {
        if (userRepository.findByEmail("admin@admin.com").isEmpty()) {
            User admin = User.builder()
                    .email("admin@admin.com") // Email jdid
                    .password(passwordEncoder.encode("admin123"))
                    .roles(Set.of("ADMIN", "USER"))
                    .build();
            userRepository.save(admin);
        }
    }

    public void register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of("USER"))
                .build();
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getEmail(), user.getRoles());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getEmail(), user.getRoles());
        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refresh(RefreshRequest request) {
        String refreshToken = request.getRefreshToken();
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        String userId = jwtUtil.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        String newAccessToken = jwtUtil.generateAccessToken(userId, user.getEmail(), user.getRoles());
        String newRefreshToken = jwtUtil.generateRefreshToken(userId, user.getEmail(), user.getRoles());
        return new AuthResponse(newAccessToken, newRefreshToken);
    }

    public void assignRole(String userId, RoleAssignmentRequest request) {
        String role = request.getRole().toUpperCase();
        if (!StringUtils.hasText(role)) {
            throw new IllegalArgumentException("Role must not be empty");
        }
        Optional<User> optional = userRepository.findById(userId);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        User user = optional.get();
        user.getRoles().add(role);
        userRepository.save(user);
    }
}