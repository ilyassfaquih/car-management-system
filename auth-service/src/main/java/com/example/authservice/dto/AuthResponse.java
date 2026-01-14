package com.example.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Response containing access and refresh tokens.
 */
@Data
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
}