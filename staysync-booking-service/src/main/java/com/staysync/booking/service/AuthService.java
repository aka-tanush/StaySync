package com.staysync.booking.service;

import java.time.LocalDateTime;
import java.util.Locale;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.staysync.booking.controller.AuthDtos.LoginRequest;
import com.staysync.booking.controller.AuthDtos.RegisterRequest;
import com.staysync.booking.controller.AuthDtos.UserResponse;
import com.staysync.booking.entity.User;
import com.staysync.booking.repository.UserRepository;

@Service
public class AuthService {

    private static final int MIN_PASSWORD_LENGTH = 8;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse register(RegisterRequest request) {
        String name = required(request.name(), "Name is required");
        String email = normalizeEmail(request.email());
        String password = required(request.password(), "Password is required");

        validateEmail(email);
        validatePassword(password);

        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("An account already exists for this email");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole("GUEST");
        user.setPhone(request.phone());
        user.setCreatedAt(LocalDateTime.now());

        return toResponse(userRepository.save(user));
    }

    public UserResponse login(LoginRequest request) {
        String email = normalizeEmail(request.email());
        String password = required(request.password(), "Password is required");

        validateEmail(email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return toResponse(user);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getPhone()
        );
    }

    private static String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }

    private static String required(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }

    private static void validateEmail(String email) {
        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("A valid email address is required");
        }
    }

    private static void validatePassword(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Password must be at least " + MIN_PASSWORD_LENGTH + " characters");
        }
    }
}
