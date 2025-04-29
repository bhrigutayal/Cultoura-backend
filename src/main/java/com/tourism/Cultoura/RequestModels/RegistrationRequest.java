package com.tourism.Cultoura.RequestModels;

public class RegistrationRequest {
    private String username;
    private String email;
    private String password;
    // Role can be "USER" or "EVENT_ORGANISER"
    private String role;

    public RegistrationRequest() {}

    // Getter and setter for username
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and setter for email
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and setter for password
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter and setter for role
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}