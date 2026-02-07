package com.cinema.controller;

import com.cinema.model.User;
import com.cinema.model.enums.UserStatus;
import com.cinema.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserModerationController {

    private final UserRepository userRepository;

    public AdminUserModerationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<User> suspend(@PathVariable long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(UserStatus.SUSPENDED);
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PatchMapping("/{id}/ban")
    public ResponseEntity<User> ban(@PathVariable long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(UserStatus.BANNED);
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PatchMapping("/{id}/unsuspend")
    public ResponseEntity<User> unsuspend(@PathVariable long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(user.getStatus() == UserStatus.ACTIVE){
            throw new RuntimeException("User is Active");
        }
        if(user.getStatus() == UserStatus.BANNED){
            throw new RuntimeException("User is Banned");
        }

        user.setStatus(UserStatus.ACTIVE);
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PatchMapping("/{id}/unban")
    public ResponseEntity<User> unban(@PathVariable long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(user.getStatus() == UserStatus.ACTIVE){
            throw new RuntimeException("User is Active");
        }
        if(user.getStatus() == UserStatus.SUSPENDED){
            throw new RuntimeException("User is Suspended");
        }

        user.setStatus(UserStatus.ACTIVE);
        return ResponseEntity.ok(userRepository.save(user));
    }
}
