package com.alumni.alumnimanagementsystem.service;

import com.alumni.alumnimanagementsystem.exception.ResourceNotFoundException;
import com.alumni.alumnimanagementsystem.model.PasswordResetToken;
import com.alumni.alumnimanagementsystem.model.User;
import com.alumni.alumnimanagementsystem.repository.PasswordResetTokenRepository;
import com.alumni.alumnimanagementsystem.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository               userRepository;
    private final BCryptPasswordEncoder        passwordEncoder;

    // FR15.3 — token expires after 24 hours
    private static final int EXPIRY_HOURS = 24;

    public PasswordResetService(
            PasswordResetTokenRepository tokenRepository,
            UserRepository               userRepository,
            BCryptPasswordEncoder        passwordEncoder) {
        this.tokenRepository = tokenRepository;
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ─────────────────────────────────────────
    // FR15.1 — Generate reset token
    // FR15.2 — token saved for email sending
    // FR15.3 — single use, expires in 24 hours
    // ─────────────────────────────────────────
    @Transactional
    public PasswordResetToken generateResetToken(String email) {

        // find user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No user found with email: " + email));

        // FR15.3 — delete all old tokens for this user
        // ensures single use per request
        tokenRepository.deleteAllTokensForUser(user);

        // FR15.3 — generate unique token
        String token = UUID.randomUUID().toString();

        // FR15.3 — set expiry to 24 hours from now
        LocalDateTime expiryDate = LocalDateTime
                .now()
                .plusHours(EXPIRY_HOURS);

        // save token
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(token);
        resetToken.setExpiryDate(expiryDate);
        resetToken.setIsUsed(false);

        return tokenRepository.save(resetToken);
    }

    // ─────────────────────────────────────────
    // FR15.3 — Validate reset token
    // must be single use and not expired
    // ─────────────────────────────────────────
    public PasswordResetToken validateToken(String token) {
        return tokenRepository
                .findValidToken(token, LocalDateTime.now())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid or expired reset token"));
    }

    // ─────────────────────────────────────────
    // FR15.3 — Reset password using token
    // FR1.3  — new password hashed with BCrypt
    // ─────────────────────────────────────────
    @Transactional
    public void resetPassword(
            String token, String newPassword) {

        // FR15.3 — validate token first
        PasswordResetToken resetToken = validateToken(token);

        // get user from token
        User user = resetToken.getUser();

        // FR1.3 — hash new password with BCrypt
        user.setPasswordHash(
                passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // FR15.3 — mark token as used
        // cannot be reused
        resetToken.setIsUsed(true);
        tokenRepository.save(resetToken);

        // FR15.3 — delete all other tokens for user
        tokenRepository.deleteExpiredTokens(
                LocalDateTime.now());
    }

    // ─────────────────────────────────────────
    // FR15.3 — Cleanup expired tokens
    // keeps DB clean
    // ─────────────────────────────────────────
    @Transactional
    public void cleanupExpiredTokens() {
        tokenRepository.deleteExpiredTokens(
                LocalDateTime.now());
    }
}