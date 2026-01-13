package com.cinema.controller;

import com.cinema.model.User;
import com.cinema.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> getMyProfile() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @PatchMapping("/me/name")
    public ResponseEntity<User> updateName(@RequestBody String name) {
        return ResponseEntity.ok(userService.updateName(name));
    }

    @PatchMapping("/me/password")
    public ResponseEntity<User> updatePassword(@RequestBody String password) {
        return ResponseEntity.ok(userService.updatePassword(password));
    }
}