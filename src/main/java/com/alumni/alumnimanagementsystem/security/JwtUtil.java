package com.alumni.alumnimanagementsystem.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

// FR1.4 — generates and validates JWT tokens for login
@Component
public class JwtUtil {

    // In production this should come from application.properties
    // (environment variable), kept simple here for the project.
    private final SecretKey secretKey =
            Keys.hmacShaKeyFor(
                "AmsSecretKeyForJwtTokenGenerationMustBe256BitsLong!".getBytes()
            );

    // FR1.4 — token valid for 24 hours
    private static final long EXPIRATION_MS = 24 * 60 * 60 * 1000;

    // ─────────────────────────────────────────
    // FR1.4 — Generate JWT token after successful login
    // token contains email (subject) and userType (claim)
    // ─────────────────────────────────────────
    public String generateToken(String email, String userType) {
        return Jwts.builder()
                .setSubject(email)
                .claim("userType", userType)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // ─────────────────────────────────────────
    // FR1.4 — Extract email from token
    // used to identify the logged-in user
    // ─────────────────────────────────────────
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // FR1.4 — Extract user type from token
    public String extractUserType(String token) {
        return getClaims(token).get("userType", String.class);
    }

    // ─────────────────────────────────────────
    // FR1.4 — Validate token
    // checks signature and expiry
    // ─────────────────────────────────────────
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}