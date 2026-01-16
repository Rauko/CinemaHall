package com.cinema.controller;

import com.cinema.model.Role;
import com.cinema.model.User;
import com.cinema.model.UserStatus;
import com.cinema.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/superadmin/users")
public class SuperAdminUserController {

    private final UserRepository userRepository;

    public SuperAdminUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/make-admin")
    public ResponseEntity<User> makeAdmin(@PathVariable long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));

        user.setRole(Role.ADMIN);
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PatchMapping("/{id}/remove-admin")
    public ResponseEntity<User> removeAdmin(@PathVariable long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));

        // SUPER_ADMIN role must be removed via DB
        if(user.getRole() == Role.SUPER_ADMIN){
            throw new RuntimeException("Can't remove role from SUPER_ADMIN.");
        }

        user.setRole(Role.USER);
        return ResponseEntity.ok(userRepository.save(user));
    }

    @DeleteMapping("/delete-banned-user")
    public ResponseEntity<String> deleteBannedUser(Long id) {
        if(userRepository.getById(id).getStatus() != UserStatus.BANNED) {
            throw new RuntimeException("User is not banned.");
        }
        userRepository.deleteUser(id);
        return ResponseEntity.ok("Banned user " + id + " deleted.");
    }

    @DeleteMapping("/delete-all-banned")
    public ResponseEntity<String> deleteAllBanned() {
        long deleted = userRepository.deleteAllByStatus(UserStatus.BANNED);
        return ResponseEntity.ok("Deleted " + deleted + " banned users.");
    }
}
