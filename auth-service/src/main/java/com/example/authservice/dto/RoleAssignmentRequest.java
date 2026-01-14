package com.example.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Payload for assigning roles to a user.
 */
@Data
public class RoleAssignmentRequest {
    @NotBlank
    private String role;
}