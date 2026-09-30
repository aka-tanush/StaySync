package com.staysync.booking.controller;

public final class AuthDtos {

    private AuthDtos() {
    }

    public record RegisterRequest(
            String name,
            String email,
            String password,
            String phone
    ) {
    }

    public record LoginRequest(
            String email,
            String password
    ) {
    }

    public record UserResponse(
            Long id,
            String name,
            String email,
            String role,
            String phone
    ) {
    }

    public record ErrorResponse(String message) {
    }
}
