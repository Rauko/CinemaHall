package com.cinema.config;

import com.cinema.exception.UserBannedException;
import com.cinema.exception.UserSuspendedException;
import com.cinema.model.User;
import com.cinema.model.enums.UserStatus;
import com.cinema.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log =
            LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {

                    log.warn("Security user not found: email={}", email);

                    return new UsernameNotFoundException(
                            "User not found: " + email
                    );
                });

        if (user.getStatus() == UserStatus.BANNED) {

            log.warn("Blocked user login attempt: userId={}, email={}",
                    user.getId(),
                    user.getEmail()
            );

            throw new UserBannedException(user.getEmail());
        }
        if (user.getStatus() == UserStatus.SUSPENDED) {

            log.warn("Suspended user login attempt: userId={}, email={}",
                    user.getId(),
                    user.getEmail()
            );

            throw new UserSuspendedException(user.getEmail());
        }

        log.debug("Security user loaded successfully: userId={}, role={}",
                user.getId(),
                user.getRole()
        );

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" +
                        user.getRole().name()))
        );
    }
}