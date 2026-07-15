package com.alumni.alumnimanagementsystem.controller;

import com.alumni.alumnimanagementsystem.model.AlumniProfile;
import com.alumni.alumnimanagementsystem.service.AlumniProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/alumni-profiles")

public class AlumniProfileController {

    private final AlumniProfileService alumniProfileService;

    public AlumniProfileController(AlumniProfileService alumniProfileService) {
        this.alumniProfileService = alumniProfileService;
    }

    // ─────────────────────────────────────────
    // FR2 — POST /api/v1/alumni-profiles/{userId}
    // Create alumni profile after registration
    // ─────────────────────────────────────────
    @PostMapping("/{userId}")
    public ResponseEntity<?> createProfile(
            @PathVariable Long userId,
            @RequestBody AlumniProfile profile) {
        try {
            AlumniProfile created =
                    alumniProfileService.createProfile(userId, profile);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // ─────────────────────────────────────────
    // FR2 — GET /api/v1/alumni-profiles/{userId}
    // ─────────────────────────────────────────
    @GetMapping("/{userId}")
    public ResponseEntity<?> getProfile(@PathVariable Long userId) {
        try {
            AlumniProfile profile =
                    alumniProfileService.getProfileByUserId(userId);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // ─────────────────────────────────────────
    // FR2 — GET /api/v1/alumni-profiles
    // ─────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<AlumniProfile>> getAllProfiles() {
        return ResponseEntity.ok(alumniProfileService.getAllProfiles());
    }

    // ─────────────────────────────────────────
    // FR2.1, FR2.2, FR2.3 — PUT /api/v1/alumni-profiles/{userId}
    // Update work history, skills, industry, location, bio
    // ─────────────────────────────────────────
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateProfile(
            @PathVariable Long userId,
            @RequestBody AlumniProfile profile) {
        try {
            AlumniProfile updated =
                    alumniProfileService.updateProfile(userId, profile);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // ─────────────────────────────────────────
    // FR2.4 — PUT /api/v1/alumni-profiles/{userId}/toggle-mentorship
    // ─────────────────────────────────────────
    @PutMapping("/{userId}/toggle-mentorship")
    public ResponseEntity<?> toggleMentorship(@PathVariable Long userId) {
        try {
            AlumniProfile updated =
                    alumniProfileService.toggleMentorship(userId);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // ─────────────────────────────────────────
    // FR2.4 — GET /api/v1/alumni-profiles/mentors
    // ─────────────────────────────────────────
    @GetMapping("/mentors")
    public ResponseEntity<List<AlumniProfile>> getAllMentors() {
        return ResponseEntity.ok(alumniProfileService.getAllMentors());
    }

    // ─────────────────────────────────────────
    // FR4.1, FR13.1 — GET /api/v1/alumni-profiles/search
    // FR13.2 — partial keyword matching
    // ─────────────────────────────────────────
    @GetMapping("/search")
    public ResponseEntity<List<AlumniProfile>> searchProfiles(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Boolean isMentor,
            @RequestParam(required = false) Integer graduationYear) {
        return ResponseEntity.ok(
                alumniProfileService.searchProfiles(
                        keyword, industry, location, isMentor, graduationYear));
    }

    // ─────────────────────────────────────────
    // FR2 — DELETE /api/v1/alumni-profiles/{userId}
    // ─────────────────────────────────────────
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteProfile(@PathVariable Long userId) {
        try {
            alumniProfileService.deleteProfile(userId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Alumni profile deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}