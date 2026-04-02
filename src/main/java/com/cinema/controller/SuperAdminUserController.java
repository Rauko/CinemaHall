package com.cinema.controller;

import com.cinema.exception.RoleChangeNotAllowedException;
import com.cinema.exception.InvalidUserStatusException;
import com.cinema.model.enums.Role;
import com.cinema.model.User;
import com.cinema.model.enums.UserStatus;
import com.cinema.repository.UserRepository;
import com.cinema.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/superadmin/users")
public class SuperAdminUserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/make-admin")
    public ResponseEntity<User> makeAdmin(@PathVariable Long id) {
        User user = userService.getCurrentUser();

        user.setRole(Role.ADMIN);
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PatchMapping("/{id}/remove-admin")
    public ResponseEntity<User> removeAdmin(@PathVariable Long id) {
        User user = userService.getCurrentUser();

        // SUPER_ADMIN role must be removed via DB
        if(user.getRole() == Role.SUPER_ADMIN){
            throw new RoleChangeNotAllowedException(user.getId());
        }

        user.setRole(Role.USER);
        return ResponseEntity.ok(userRepository.save(user));
    }

    @DeleteMapping("/{id}/delete-banned-user")
    public ResponseEntity<String> deleteBannedUser(@PathVariable Long id) {
        if(userService.getUserById(id).getStatus() != UserStatus.BANNED) {
            throw new InvalidUserStatusException(
                    userService.getUserById(id).getId(),
                    UserStatus.BANNED
            );
        }
        userService.deleteUser(id);
        return ResponseEntity.ok("Banned user " + id + " deleted.");
    }

    @DeleteMapping("/delete-all-banned-users")
    public ResponseEntity<String> deleteAllBanned() {
        long deleted = userRepository.deleteAllByStatus(UserStatus.BANNED);
        return ResponseEntity.ok("Deleted " + deleted + " banned users.");
    }
}
