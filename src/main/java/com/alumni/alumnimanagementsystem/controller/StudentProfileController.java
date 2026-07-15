package com.alumni.alumnimanagementsystem.controller;

import com.alumni.alumnimanagementsystem.model.StudentProfile;
import com.alumni.alumnimanagementsystem.service.StudentProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/student-profiles")

public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    public StudentProfileController(StudentProfileService studentProfileService) {
        this.studentProfileService = studentProfileService;
    }

    // ─────────────────────────────────────────
    // FR3 — POST /api/v1/student-profiles/{userId}
    // Create student profile after registration
    // ─────────────────────────────────────────
    @PostMapping("/{userId}")
    public ResponseEntity<?> createProfile(
            @PathVariable Long userId,
            @RequestBody StudentProfile profile) {
        try {
            StudentProfile created =
                    studentProfileService.createProfile(userId, profile);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // ─────────────────────────────────────────
    // FR3 — GET /api/v1/student-profiles/{userId}
    // ─────────────────────────────────────────
    @GetMapping("/{userId}")
    public ResponseEntity<?> getProfile(@PathVariable Long userId) {
        try {
            StudentProfile profile =
                    studentProfileService.getProfileByUserId(userId);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // ─────────────────────────────────────────
    // FR3 — GET /api/v1/student-profiles
    // ─────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<StudentProfile>> getAllProfiles() {
        return ResponseEntity.ok(studentProfileService.getAllProfiles());
    }

    // ─────────────────────────────────────────
    // FR3.1, FR3.2 — PUT /api/v1/student-profiles/{userId}
    // Update academic interests, career goals, bio, skills
    // ─────────────────────────────────────────
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateProfile(
            @PathVariable Long userId,
            @RequestBody StudentProfile profile) {
        try {
            StudentProfile updated =
                    studentProfileService.updateProfile(userId, profile);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // ─────────────────────────────────────────
    // FR3.3 — PUT /api/v1/student-profiles/{userId}/toggle-mentorship
    // ─────────────────────────────────────────
    @PutMapping("/{userId}/toggle-mentorship")
    public ResponseEntity<?> toggleMentorship(@PathVariable Long userId) {
        try {
            StudentProfile updated =
                    studentProfileService.toggleMentorship(userId);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // ─────────────────────────────────────────
    // FR3.3 — GET /api/v1/student-profiles/mentees
    // ─────────────────────────────────────────
    @GetMapping("/mentees")
    public ResponseEntity<List<StudentProfile>> getAllMentees() {
        return ResponseEntity.ok(studentProfileService.getAllMentees());
    }

    // ─────────────────────────────────────────
    // FR4.1, FR13.1 — GET /api/v1/student-profiles/search
    // FR13.2 — partial keyword matching
    // ─────────────────────────────────────────
    @GetMapping("/search")
    public ResponseEntity<List<StudentProfile>> searchProfiles(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean isMentee,
            @RequestParam(required = false) Integer expectedGraduationYear) {
        return ResponseEntity.ok(
                studentProfileService.searchProfiles(
                        keyword, isMentee, expectedGraduationYear));
    }

    // ─────────────────────────────────────────
    // FR3 — DELETE /api/v1/student-profiles/{userId}
    // ─────────────────────────────────────────
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteProfile(@PathVariable Long userId) {
        try {
            studentProfileService.deleteProfile(userId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Student profile deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}