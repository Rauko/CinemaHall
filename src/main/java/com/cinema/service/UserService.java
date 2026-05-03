package com.cinema.service;

import com.cinema.dto.RegisterRequest;
import com.cinema.exception.EmailAlreadyExistsException;
import com.cinema.exception.EmailBelongsToBannedUserException;
import com.cinema.exception.UnauthenticatedException;
import com.cinema.exception.UserNotFoundException;
import com.cinema.model.BlockedEmail;
import com.cinema.model.User;
import com.cinema.model.enums.UserStatus;
import com.cinema.repository.BlockedEmailRepository;
import com.cinema.repository.UserRepository;
import com.cinema.util.EmailNormalizer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

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
            log.warn("Unauthenticated access attempt");
            throw new UnauthenticatedException();
        }

        return getUserByEmail(auth.getName());
    }


    public User updateName(String newName) {
        User user = getCurrentUser();

        log.info("Updating user name: userId={}, oldName={}, newName={}",
                user.getId(),
                user.getName(),
                newName
        );

        user.setName(newName);

        log.info("User name updated: userId={}", user.getId());

        return userRepository.save(user);
    }

    public User updateEmail(String newEmail) {
        User user = getCurrentUser();

        String normalizedEmail = EmailNormalizer.normalize(newEmail);

        log.info("Updating user email: userId={}, oldEmail={}, newEmail={}",
                user.getId(),
                user.getEmail(),
                normalizedEmail
        );

        user.setEmail(normalizedEmail);

        userRepository.save(user);

        log.info("User email updated: userId={}", user.getId());

        return user;
    }

    public User updatePassword(String newPassword) {
        User user = getCurrentUser();

        log.info("Updating user password: userId={}", user.getId());

        user.setPasswordHash(passwordEncoder.encode(newPassword));

        userRepository.save(user);

        log.info("User password updated: userId={}", user.getId());

        return user;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);

        log.info("User {} has been deleted.", id);
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

        log.info("User {}({}) has been registered.",
                user.getId(),
                user.getEmail()
        );

        return userRepository.save(user);
    }

    public void blockUser(Long id, String reason) {

        User user = getUserById(id);

        log.info("Blocking user: userId={}, email={}, reason={}",
                user.getId(),
                user.getEmail(),
                reason
        );

        user.setStatus(UserStatus.BANNED);

        String normalizedEmail = EmailNormalizer.normalize(user.getEmail());

        if(!blockedEmailRepository.existsByEmail(normalizedEmail)) {
            BlockedEmail blockedEmail = new BlockedEmail();
            blockedEmail.setEmail(normalizedEmail);
            blockedEmail.setReason(reason);
            blockedEmail.setBlockedAt(LocalDateTime.now());

            blockedEmailRepository.save(blockedEmail);

            log.info("Blocked email added: userId={}, email={}",
                    user.getId(),
                    normalizedEmail
            );
        }
        userRepository.save(user);

        log.info("User banned successfully: userId={}", user.getId());
    }
}
