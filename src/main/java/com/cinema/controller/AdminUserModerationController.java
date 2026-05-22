package com.cinema.controller;

import com.cinema.exception.InvalidUserStatusException;
import com.cinema.model.User;
import com.cinema.model.enums.UserStatus;
import com.cinema.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
public class AdminUserModerationController {

    private static final Logger log =
            LoggerFactory.getLogger(AdminUserModerationController.class);

    private final UserService userService;

    @PatchMapping("/{id}/suspend")
    public ResponseEntity<User> suspend(@PathVariable Long id){

        User admin = userService.getCurrentUser();

        log.info("ADMIN ACTION by {}({}): action=SUSPEND targetUserId={}",
                 admin.getName(),
                 admin.getId(),
                 id);

        User user = userService.getUserById(id);

        user.setStatus(UserStatus.SUSPENDED);

        log.info("ADMIN ACTION by {}({}): User suspended targetUserId={}",
                 admin.getName(),
                 admin.getId(),
                 id);

        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{id}/ban")
    public ResponseEntity<User> ban(@PathVariable Long id){

        User admin = userService.getCurrentUser();

        log.info("ADMIN ACTION by {}({}): action=BAN targetUserId={}",
                 admin.getName(),
                 admin.getId(),
                 id);

        User user = userService.getUserById(id);

        user.setStatus(UserStatus.BANNED);

        log.info("ADMIN ACTION by {}({}): User banned targetUserId={}",
                 admin.getName(),
                 admin.getId(),
                 id);

        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{id}/unsuspend")
    public ResponseEntity<User> unsuspend(@PathVariable Long id){

        User admin = userService.getCurrentUser();

        log.info("ADMIN ACTION by {}({}): action=UNSUSPEND targetUserId={}",
                 admin.getName(),
                 admin.getId(),
                 id);

        User user = userService.getUserById(id);

        if (user.getStatus() == UserStatus.ACTIVE) {

            log.warn("ADMIN ACTION by {}({}): Invalid UNSUSPEND attempt targetUserId={}, currentStatus={}",
                     admin.getName(),
                     admin.getId(),
                     id,
                     user.getStatus());

            throw new InvalidUserStatusException(id, UserStatus.SUSPENDED);
        }

        if (user.getStatus() == UserStatus.BANNED) {

            log.warn("ADMIN ACTION by {}({}): Invalid UNSUSPEND attempt targetUserId={}, currentStatus={}",
                     admin.getName(),
                     admin.getId(),
                     id,
                     user.getStatus());

            throw new InvalidUserStatusException(id, UserStatus.SUSPENDED);
        }

        user.setStatus(UserStatus.ACTIVE);

        log.info("ADMIN ACTION by {}({}): User unsuspended targetUserId={}",
                 admin.getName(),
                 admin.getId(),
                 id);

        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{id}/unban")
    public ResponseEntity<User> unban(@PathVariable Long id){

        User admin = userService.getCurrentUser();

        log.info("ADMIN ACTION by {}({}): action=UNBAN targetUserId={}",
                 admin.getName(),
                 admin.getId(),
                 id);

        User user = userService.getUserById(id);

        if (user.getStatus() == UserStatus.ACTIVE) {

            log.warn("ADMIN ACTION by {}({}): Invalid UNBAN attempt targetUserId={}, currentStatus={}",
                     admin.getName(),
                     admin.getId(),
                     id,
                     user.getStatus());

            throw new InvalidUserStatusException(id, UserStatus.BANNED);
        }

        if (user.getStatus() == UserStatus.SUSPENDED) {

            log.warn("ADMIN ACTION by {}({}): Invalid UNBAN attempt targetUserId={}, currentStatus={}",
                     admin.getName(),
                     admin.getId(),
                     id,
                     user.getStatus());

            throw new InvalidUserStatusException(id, UserStatus.BANNED);
        }

        user.setStatus(UserStatus.ACTIVE);

        log.info("ADMIN ACTION by {}({}): User unbanned targetUserId={}",
                 admin.getName(),
                 admin.getId(),
                 id);

        return ResponseEntity.ok(user);
    }
}
