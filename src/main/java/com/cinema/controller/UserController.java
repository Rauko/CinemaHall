package com.cinema.controller;

import com.cinema.dto.user.UserDto;
import com.cinema.dto.user.mapper.UserMapper;
import com.cinema.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMyProfile() {
        return ResponseEntity.ok(UserMapper.toDto(userService.getCurrentUser()));
    }

    @PatchMapping("/me/name")
    public ResponseEntity<UserDto> updateName(@RequestBody String name) {
        return ResponseEntity.ok(UserMapper.toDto(userService.updateName(name)));
    }

    @PatchMapping("/me/password")
    public ResponseEntity<UserDto> updatePassword(@RequestBody String password) {
        return ResponseEntity.ok(UserMapper.toDto(userService.updatePassword(password)));
    }
}