package com.staysync.auth.service;

import com.staysync.auth.entity.User;
import com.staysync.auth.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String name, String email, String password, String requestedRole) {

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }

        String encryptedPassword = passwordEncoder.encode(password);

        User user = new User(
                name,
                email,
                encryptedPassword
        );
        user.setRole(normalizeRole(requestedRole));

        return userRepository.save(user);
    }

    private String normalizeRole(String requestedRole) {
        String role = requestedRole == null || requestedRole.isBlank()
                ? "GUEST"
                : requestedRole.trim().toUpperCase(Locale.ROOT);

        if (!role.equals("GUEST")) {
            throw new IllegalArgumentException(
                "Public registration can only create GUEST accounts."
            );
        }

        return role;
    }

    public User login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return user;
    }
}