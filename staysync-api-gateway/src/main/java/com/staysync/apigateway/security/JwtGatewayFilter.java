package com.staysync.apigateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtGatewayFilter implements GlobalFilter, Ordered {

    private final SecretKey signingKey;

    public JwtGatewayFilter(@Value("${jwt.secret}") String secret) {
        this.signingKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {

        HttpMethod method = exchange.getRequest().getMethod();
        String path = exchange.getRequest().getPath().value();

        // Public endpoints
        if (method == HttpMethod.OPTIONS
                || isPublicRoomRead(method, path)
                || isPublicAuthEndpoint(method, path)) {
            return chain.filter(exchange);
        }

        // Get JWT from Authorization header
        String authorization = exchange.getRequest()
                .getHeaders()
                .getFirst("Authorization");

        if (authorization == null
                || !authorization.startsWith("Bearer ")) {
            return reject(exchange, HttpStatus.UNAUTHORIZED);
        }

        // Validate JWT
        Claims claims;

        try {
            claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(authorization.substring(7))
                    .getPayload();

        } catch (RuntimeException exception) {
            return reject(exchange, HttpStatus.UNAUTHORIZED);
        }

        // Admin-only endpoints
        if (isAdminOnly(method, path)
                && !"ADMIN".equalsIgnoreCase(
                        String.valueOf(claims.get("role")))) {

            return reject(exchange, HttpStatus.FORBIDDEN);
        }

        // Users can only access their own bookings
        if (path.matches(
                "/staysync-booking-service/api/bookings/user/[^/]+")) {

            String requestedUserId =
                    path.substring(path.lastIndexOf('/') + 1);

            String tokenUserId =
                    String.valueOf(claims.get("userId"));

            if (!requestedUserId.equals(tokenUserId)) {
                return reject(exchange, HttpStatus.FORBIDDEN);
            }
        }

        return chain.filter(exchange);
    }

    /**
     * GET requests to Room Service are public.
     */
    private boolean isPublicRoomRead(
            HttpMethod method,
            String path) {

        return method == HttpMethod.GET
                && path.startsWith("/staysync-room-service/");
    }

    /**
     * Login and registration are public because
     * the user does not have a JWT before logging in.
     */
    private boolean isPublicAuthEndpoint(
            HttpMethod method,
            String path) {

        return method == HttpMethod.POST
                && (path.equals(
                        "/staysync-auth-service/api/auth/login")
                || path.equals(
                        "/staysync-auth-service/api/auth/register"));
    }

    /**
     * Determines which endpoints require ADMIN role.
     */
    private boolean isAdminOnly(
            HttpMethod method,
            String path) {

        // Room modifications require ADMIN.
        if (path.startsWith("/staysync-room-service/")) {
            return method != HttpMethod.GET;
        }

        // A user accessing their own bookings is not admin-only.
        if (path.matches(
                "/staysync-booking-service/api/bookings/user/[^/]+")) {
            return false;
        }

        // Booking GET endpoints are ADMIN-only.
        return (path.equals(
                    "/staysync-booking-service/api/bookings")
                && method == HttpMethod.GET)
                || (path.startsWith(
                    "/staysync-booking-service/api/bookings/")
                && method == HttpMethod.GET);
    }

    /**
     * Returns the requested HTTP error status.
     */
    private Mono<Void> reject(
            ServerWebExchange exchange,
            HttpStatus status) {

        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -100;
    }
}