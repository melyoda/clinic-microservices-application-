package com.mawkly.auth_service.dto;


import lombok.Data;

@Data
public class AuthValidationResponse {
    private boolean isValid;
    private String userId;    // Important for later (who is making the request?)
    private String username;
    private String role;      // Important for authorization (e.g., "ROLE_DOCTOR")

    // Helper constructor
    public AuthValidationResponse(boolean isValid, String userId, String username, String role) {
        this.isValid = isValid;
        this.userId = userId;
        this.username = username;
        this.role = role;
    }
}