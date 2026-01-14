package com.example.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request payload for refreshing an access token.
 */
@Data
public class RefreshRequest {
    @NotBlank
    private String refreshToken;
}