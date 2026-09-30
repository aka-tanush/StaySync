package com.staysync.auth.controller;

import com.staysync.auth.entity.User;
import com.staysync.auth.service.AuthService;
import com.staysync.auth.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        try {
            User user = authService.register(
                    request.name(),
                    request.email(),
                    request.password(),
                    request.role()
            );

            return ResponseEntity.ok(
                    new AuthResponse(
                            user.getId(),
                            user.getName(),
                            user.getEmail(),
                            user.getRole(),
                            jwtService.generateToken(user),
                            "Registration successful"
                    )
            );

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        try {
            User user = authService.login(
                    request.email(),
                    request.password()
            );

            return ResponseEntity.ok(
                    new AuthResponse(
                            user.getId(),
                            user.getName(),
                            user.getEmail(),
                            user.getRole(),
                            jwtService.generateToken(user),
                            "Login successful"
                    )
            );

        } catch (RuntimeException e) {
            return ResponseEntity.status(401)
                    .body(e.getMessage());
        }
    }

    public record RegisterRequest(
            String name,
            String email,
            String password,
            String phone,
            String role
    ) {
    }

    public record LoginRequest(
            String email,
            String password
    ) {
    }

    public record AuthResponse(
            Long id,
            String name,
            String email,
            String role,
            String token,
            String message
    ) {
    }
}