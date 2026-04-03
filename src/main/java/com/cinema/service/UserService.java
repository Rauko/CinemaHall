package com.cinema.service;

import com.cinema.dto.RegisterRequest;
import com.cinema.exception.EmailAlreadyExistsException;
import com.cinema.exception.EmailBelongsToBannedUserException;
import com.cinema.exception.UserNotFoundException;
import com.cinema.model.BlockedEmail;
import com.cinema.model.User;
import com.cinema.model.enums.UserStatus;
import com.cinema.repository.BlockedEmailRepository;
import com.cinema.repository.UserRepository;
import com.cinema.util.EmailNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BlockedEmailRepository blockedEmailRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public User getCurrentUser() {
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            throw new RuntimeException("No authenticated user");
        }

        return getUserByEmail(auth.getName());
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

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User register(RegisterRequest request) {
        String normalizedEmail = EmailNormalizer.normalize(request.getEmail());

        if(userRepository.findByEmail(normalizedEmail).isPresent()) {
            if (blockedEmailRepository.existsByEmail(normalizedEmail)) {
                throw new EmailBelongsToBannedUserException(normalizedEmail);
            } else throw new EmailAlreadyExistsException();
        }

        User user = new User();
        user.setEmail(normalizedEmail);
        user.setName(request.getName());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setStatus(UserStatus.ACTIVE);

        return userRepository.save(user);
    }

    public void blockUser(Long id, String reason) {

        User user = getUserById(id);

        user.setStatus(UserStatus.BANNED);

        String normalizedEmail = EmailNormalizer.normalize(user.getEmail());

        if(!blockedEmailRepository.existsByEmail(normalizedEmail)) {
            BlockedEmail blockedEmail = new BlockedEmail();
            blockedEmail.setEmail(normalizedEmail);
            blockedEmail.setReason(reason);
            blockedEmail.setBlockedDate(LocalDateTime.now());

            blockedEmailRepository.save(blockedEmail);
        }
        userRepository.save(user);
    }
}
