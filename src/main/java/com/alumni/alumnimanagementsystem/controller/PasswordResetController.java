package com.alumni.alumnimanagementsystem.controller;

import com.alumni.alumnimanagementsystem.model.PasswordResetToken;
import com.alumni.alumnimanagementsystem.service.PasswordResetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")

public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    // ─────────────────────────────────────────
    // FR15.1 — POST /api/v1/auth/forgot-password
    // FR15.2 — generates token (email sending is
    //          out of scope for backend-only build;
    //          token would normally be emailed here)
    // FR15.3 — token expires in 24 hours, single use
    // ─────────────────────────────────────────
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestParam String email) {
        try {
            PasswordResetToken resetToken =
                    passwordResetService.generateResetToken(email);

            // NOTE: In production, only a success message
            // is returned here, and the token is sent via
            // email. Returning the token directly is for
            // local testing/Postman verification only.
            Map<String, String> response = new HashMap<>();
            response.put("message", "Password reset token generated");
            response.put("token", resetToken.getToken());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // ─────────────────────────────────────────
    // FR15.3 — GET /api/v1/auth/validate-reset-token
    // check if token is valid before showing reset form
    // ─────────────────────────────────────────
    @GetMapping("/validate-reset-token")
    public ResponseEntity<?> validateToken(
            @RequestParam String token) {
        try {
            passwordResetService.validateToken(token);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Token is valid");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // ─────────────────────────────────────────
    // FR15.3 — POST /api/v1/auth/reset-password
    // resets password using valid token
    // FR1.3 — new password hashed with BCrypt
    // ─────────────────────────────────────────
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {
        try {
            passwordResetService.resetPassword(token, newPassword);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Password reset successful");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}