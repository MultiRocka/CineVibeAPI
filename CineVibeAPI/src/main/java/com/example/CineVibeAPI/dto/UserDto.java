package com.example.CineVibeAPI.dto;

import lombok.Getter;

@Getter
public class UserDto {

    private String username;
    private String email;

    public UserDto(String username, String email) {
        this.username = username;
        this.email = email;
    }

    // Gettery i settery

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
