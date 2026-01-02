package com.cinema.controller;

import com.cinema.service.AuthService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //registration
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        String token = authService.register(
                request.getName(),
                request.getEmail(),
                request.getPassword());
        return ResponseEntity.ok(token);
    }

    //login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        String token = authService.login(
                request.getEmail(),
                request.getPassword());
        return ResponseEntity.ok(token);
    }

    //DTO for registration
    @Data
    public class RegisterRequest {
        private String name;
        private String email;
        private String password;
    }

    @Data
    public class LoginRequest {
        private String email;
        private String password;
    }

    @Data
    public class JwtResponse {
        private final String token;
    }
}
