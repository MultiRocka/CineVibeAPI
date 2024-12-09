package com.example.CineVibeAPI.controller;

import com.example.CineVibeAPI.dto.PasswordChangeRequest;
import com.example.CineVibeAPI.dto.UserDto;
import com.example.CineVibeAPI.model.User;
import com.example.CineVibeAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public ResponseEntity<UserDto> getUserInfo() {
        User user = userService.getCurrentUser(); // Pobranie aktualnego użytkownika
        UserDto userDTO = new UserDto(user.getUsername(), user.getEmail()); // Tworzenie DTO z danych użytkownika
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/user/password")
    public ResponseEntity<Void> updatePassword(@RequestBody PasswordChangeRequest request) {
        userService.changePassword(request.getPassword()); // Zmiana hasła
        return ResponseEntity.ok().build();
    }
}
