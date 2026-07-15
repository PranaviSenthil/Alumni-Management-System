package com.alumni.alumnimanagementsystem.controller;

import com.alumni.alumnimanagementsystem.model.User;
import com.alumni.alumnimanagementsystem.security.JwtUtil;
import com.alumni.alumnimanagementsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import com.alumni.alumnimanagementsystem.security.JwtUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")

public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UserService userService, JwtUtil jwtUtil,
    BCryptPasswordEncoder passwordEncoder) {


    this.userService     = userService;
    this.jwtUtil         = jwtUtil;
    this.passwordEncoder = passwordEncoder;
}

    // ─────────────────────────────────────────
    // FR1.1, FR1.2, FR1.3, FR1.5
    // POST /api/v1/auth/register
    // Register new user (Alumnus or Student)
    // ─────────────────────────────────────────
    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        try {
            User saved = userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    
    // Admin — get all users
    
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    
    
    // Admin — get user by id
    
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    
    // Admin — search users by name or email
    
    @GetMapping("/users/search")
    public ResponseEntity<List<User>> searchUsers(
            @RequestParam String keyword) {
        return ResponseEntity.ok(userService.searchUsers(keyword));
    }

    
    // Admin — filter users by type
   
    @GetMapping("/users/type/{userType}")
    public ResponseEntity<List<User>> getUsersByType(
            @PathVariable User.UserType userType) {
        return ResponseEntity.ok(userService.getUsersByType(userType));
    }

    
    // Update user profile
    
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody User user) {
        try {
            User updated = userService.updateUser(id, user);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    
    // Soft delete — profile invisible in search
    
    @DeleteMapping("/users/{id}/deactivate")
    public ResponseEntity<?> deactivateUser(@PathVariable Long id) {
        try {
            userService.deactivateUser(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User deactivated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    
    // Permanent delete — irreversible
    
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User permanently deleted");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    
    // Admin — verify user account
    
    @PutMapping("/admin/users/{id}/verify")
    public ResponseEntity<?> verifyUser(@PathVariable Long id) {
        try {
            User verified = userService.verifyUser(id);
            return ResponseEntity.ok(verified);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    
// Validates email + password, returns JWT token

@PostMapping("/auth/login")
public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
    String email    = loginRequest.get("email");
    String password = loginRequest.get("password");

    return userService.getUserByEmail(email)
            .filter(user -> passwordEncoder.matches(password, user.getPasswordHash()))
            .map(user -> {
                String token = jwtUtil.generateToken(email, user.getUserType().name());
                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("email", user.getEmail());
                response.put("userType", user.getUserType());
                response.put("userId", user.getId());
                return ResponseEntity.ok((Object) response);
            })
            .orElseGet(() -> {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Invalid email or password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            });
}

    // ─────────────────────────────────────────
    // Validation error handler
    // NFR 3.3.3 — meaningful JSON error messages
    // ─────────────────────────────────────────
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return errors;
    }
}