package com.cinema.controller;

import com.cinema.exception.InvalidUserStatusException;
import com.cinema.model.User;
import com.cinema.model.enums.UserStatus;
import com.cinema.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
public class AdminUserModerationController {

    private final UserService userService;

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<User> suspend(@PathVariable Long id){
        User user = userService.getUserById(id);

        user.setStatus(UserStatus.SUSPENDED);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{id}/ban")
    public ResponseEntity<User> ban(@PathVariable Long id){
        User user = userService.getUserById(id);

        user.setStatus(UserStatus.BANNED);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{id}/unsuspend")
    public ResponseEntity<User> unsuspend(@PathVariable Long id){
        User user = userService.getUserById(id);

        if(user.getStatus() == UserStatus.ACTIVE){
            throw new InvalidUserStatusException(id, UserStatus.SUSPENDED);
        }
        if(user.getStatus() == UserStatus.BANNED){
            throw new InvalidUserStatusException(id, UserStatus.SUSPENDED);
        }

        user.setStatus(UserStatus.ACTIVE);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{id}/unban")
    public ResponseEntity<User> unban(@PathVariable Long id){
        User user = userService.getUserById(id);

        if(user.getStatus() == UserStatus.ACTIVE){
            throw new InvalidUserStatusException(id, UserStatus.BANNED);
        }
        if(user.getStatus() == UserStatus.SUSPENDED){
            throw new InvalidUserStatusException(id, UserStatus.BANNED);
        }

        user.setStatus(UserStatus.ACTIVE);
        return ResponseEntity.ok(user);
    }
}
