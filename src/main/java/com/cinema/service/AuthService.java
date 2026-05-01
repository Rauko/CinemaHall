package com.cinema.service;

import com.cinema.config.JwtUtils;
import com.cinema.exception.EmailAlreadyExistsException;
import com.cinema.model.User;
import com.cinema.model.enums.Role;
import com.cinema.model.enums.UserStatus;
import com.cinema.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    //new user registration
    public String register(String name, String email, String password) {

        log.info("Registration attempt: email={}", email);

        if(userRepository.existsByEmail(email)){
            log.warn("Registration failed: email already exists, email={}", email);
            throw new EmailAlreadyExistsException();
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(Role.USER);
        user.setStatus(UserStatus.ACTIVE);

        userRepository.save(user);

        log.info("Registration successful: userId={}, email={}",
                user.getId(),
                user.getEmail()
        );

        return jwtUtils.generateToken(
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getEmail())
                        .password(user.getPasswordHash())
                        .roles(user.getRole().name())
                        .build()
        );
    }

    //login
    public String login(String email, String password) {

        log.info("Login attempt: email={}", email);

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            log.info("Login successful: email={}", email);

            return jwtUtils.generateToken(userDetails);

        } catch (BadCredentialsException e) {

            log.warn("Login failed: invalid credentials, email={}", email);

            throw e;
        }
    }
}
