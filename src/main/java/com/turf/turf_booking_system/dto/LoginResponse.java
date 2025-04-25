package com.turf.turf_booking_system.dto;

public class LoginResponse {

    private Long userId;
    private String email;
    private String message;
    private String role;
    private String token;

    // No-arg constructor
    public LoginResponse() {
        // Default initialization (optional)
        this.message = "No message provided";
    }

    // Parameterized constructor
    public LoginResponse(Long userId, String email, String message, String role, String token) {
        this.userId = userId;
        this.email = email;
        this.message = message;
        this.role = role;
        this.token = token;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    // Override toString for better debugging
    @Override
    public String toString() {
        return "LoginResponse{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", message='" + message + '\'' +
                ", role='" + role + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
