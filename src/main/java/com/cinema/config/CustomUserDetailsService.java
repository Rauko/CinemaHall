package com.cinema.config;

import com.cinema.exception.UserBannedException;
import com.cinema.exception.UserSuspendedException;
import com.cinema.model.User;
import com.cinema.model.enums.UserStatus;
import com.cinema.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        if (user.getStatus() == UserStatus.BANNED) {
            throw new UserBannedException(user.getEmail());
        }
        if (user.getStatus() == UserStatus.SUSPENDED) {
            throw new UserSuspendedException(user.getEmail());
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" +
                        user.getRole().name()))
        );
    }
}