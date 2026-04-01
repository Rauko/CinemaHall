package com.cinema.config;

import com.cinema.exception.UserBannedException;
import com.cinema.exception.UserSuspendedException;
import com.cinema.model.User;
import com.cinema.model.enums.UserStatus;
import com.cinema.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.getUserByEmail(email);

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