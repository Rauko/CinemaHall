package com.cinema.controller;

import com.cinema.exception.RoleChangeNotAllowedException;
import com.cinema.exception.InvalidUserStatusException;
import com.cinema.model.enums.Role;
import com.cinema.model.User;
import com.cinema.model.enums.UserStatus;
import com.cinema.repository.UserRepository;
import com.cinema.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/superadmin/users")
public class SuperAdminUserController {
    private static final Logger log = LoggerFactory.getLogger(SuperAdminUserController.class);
    private final UserRepository userRepository;
    private final UserService userService;

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        User admin = userService.getCurrentUser();

        log.info("SUPERADMIN ACTION by {}({}): action=DELETE_USER targetUserId={}",
                 admin.getName(),
                 admin.getId(),
                 id);

        userRepository.deleteById(id);

        log.info("SUPERADMIN ACTION by {}({}): User deleted targetUserId={}",
                 admin.getName(),
                 admin.getId(),
                 id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/make-admin")
    public ResponseEntity<User> makeAdmin(@PathVariable Long id) {
        User admin = userService.getCurrentUser();
        User target = userService.getUserById(id);

        log.info("SUPERADMIN ACTION by {}({}): action=MAKE_ADMIN targetUserId={}",
                 admin.getName(),
                 admin.getId(),
                 target.getId());

        target.setRole(Role.ADMIN);
        userRepository.save(target);

        log.info("SUPERADMIN ACTION by {}({}): User promoted to ADMIN targetUserId={}",
                 admin.getName(),
                 admin.getId(),
                 target.getId());

        return ResponseEntity.ok(target);
    }

    @PatchMapping("/{id}/remove-admin")
    public ResponseEntity<User> removeAdmin(@PathVariable Long id) {
        User target = userService.getUserById(id);
        User admin = userService.getCurrentUser();

        log.info("SUPERADMIN ACTION by {}({}): action=REMOVE_ADMIN targetUserId={}",
                 admin.getName(),
                 admin.getId(),
                 target.getId()
        );

        if(target.getRole() == Role.SUPER_ADMIN){
            log.warn("SUPERADMIN ACTION by {}({}): Forbidden role change attempt targetUserId={}, role={}. " +
                            "SUPER_ADMIN role must be removed via DB.",
                     admin.getName(),
                     admin.getId(),
                     target.getId(),
                     target.getRole());

            throw new RoleChangeNotAllowedException(target.getId());
        }

        target.setRole(Role.USER);
        userRepository.save(target);

        log.info("SUPERADMIN ACTION by {}({}): ADMIN role removed targetUserId={}",
                 admin.getName(),
                 admin.getId(),
                 target.getId());

        return ResponseEntity.ok(target);
    }

    @DeleteMapping("/{id}/delete-banned-user")
    public ResponseEntity<String> deleteBannedUser(@PathVariable Long id) {
        User admin = userService.getCurrentUser();
        User target = userService.getUserById(id);

        log.info("SUPERADMIN ACTION by {}({}): action=DELETE_BANNED_USER targetUserId={}",
                 admin.getName(),
                 admin.getId(),
                 target.getId());

        if (target.getStatus() != UserStatus.BANNED) {
            log.warn("SUPERADMIN ACTION by {}({}): Invalid banned-user deletion attempt targetUserId={}, status={}",
                     admin.getName(),
                     admin.getId(),
                     target.getId(),
                     target.getStatus());

            throw new InvalidUserStatusException(target.getId(),UserStatus.BANNED
            );
        }

        userService.deleteUser(id);

        log.info("SUPERADMIN ACTION by {}({}): Banned user deleted targetUserId={}",
                 admin.getName(),
                 admin.getId(),
                 target.getId()
        );

        return ResponseEntity.ok("Banned user " + id + " deleted.");
    }

    @DeleteMapping("/delete-all-banned-users")
    public ResponseEntity<String> deleteAllBanned() {
        User admin = userService.getCurrentUser();

        log.info("SUPERADMIN ACTION by {}({}): action=DELETE_ALL_BANNED_USERS",
                admin.getName(),
                admin.getId());

        long deleted = userRepository.deleteAllByStatus(UserStatus.BANNED);

        log.info("SUPERADMIN ACTION by {}({}): Deleted banned users count={}",
                 admin.getName(),
                 admin.getId(),
                 deleted);

        return ResponseEntity.ok("Deleted " + deleted + " banned users.");
    }
}
