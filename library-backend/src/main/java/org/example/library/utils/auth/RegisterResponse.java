package org.example.library.utils.auth;

import java.util.UUID;

public class RegisterResponse {
    private UUID userId;
    private String message;

    public RegisterResponse(UUID userId) {
        this.userId = userId;
        this.message = "User registered successfully";
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

