package com.cinema.service;

import com.cinema.model.User;
import com.cinema.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getName() == null) {
            throw new RuntimeException("Unauthorized");
        }

        String email = auth.getName();
        return getUserByEmail(email);
    }

    public User updateName(String newName) {
        User user = getCurrentUser();
        user.setName(newName);
        return userRepository.save(user);
    }

    public User updateEmail(String newEmail) {
        User user = getCurrentUser();
        user.setEmail(newEmail);
        return userRepository.save(user);
    }

    public User updatePassword(String newPassword) {
        User user = getCurrentUser();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }
}
