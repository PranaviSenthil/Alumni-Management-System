package com.alumni.alumnimanagementsystem.service;

import com.alumni.alumnimanagementsystem.exception.ResourceNotFoundException;
import com.alumni.alumnimanagementsystem.model.StudentProfile;
import com.alumni.alumnimanagementsystem.model.User;
import com.alumni.alumnimanagementsystem.repository.StudentProfileRepository;
import com.alumni.alumnimanagementsystem.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final UserRepository userRepository;

    public StudentProfileService(
            StudentProfileRepository studentProfileRepository,
            UserRepository userRepository) {
        this.studentProfileRepository = studentProfileRepository;
        this.userRepository           = userRepository;
    }

    // ─────────────────────────────────────────
    // FR3 — Create student profile
    // called after user registers as STUDENT
    // ─────────────────────────────────────────
    @Transactional
    public StudentProfile createProfile(
            Long userId, StudentProfile profile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));

        // make sure user is STUDENT
        if (user.getUserType() != User.UserType.STUDENT) {
            throw new IllegalArgumentException(
                    "User is not a Student");
        }

        profile.setUser(user);
        return studentProfileRepository.save(profile);
    }

    // ─────────────────────────────────────────
    // FR3 — Get student profile by user id
    // ─────────────────────────────────────────
    public StudentProfile getProfileByUserId(Long userId) {
        return studentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student profile not found for user id: "
                                + userId));
    }

    // ─────────────────────────────────────────
    // FR3 — Get all student profiles
    // ─────────────────────────────────────────
    public List<StudentProfile> getAllProfiles() {
        return studentProfileRepository.findAll();
    }

    // ─────────────────────────────────────────
    // FR3.1 — Update academic interests
    // FR3.1 — Update career goals
    // FR3.1 — Update bio
    // FR3.2 — Update skills
    // ─────────────────────────────────────────
    @Transactional
    public StudentProfile updateProfile(
            Long userId, StudentProfile updatedProfile) {
        StudentProfile existing = getProfileByUserId(userId);

        // FR3.1 — academic interests
        existing.setAcademicInterests(
                updatedProfile.getAcademicInterests());

        // FR3.1 — career goals
        existing.setCareerGoals(
                updatedProfile.getCareerGoals());

        // FR3.1 — bio
        existing.setBio(updatedProfile.getBio());

        // FR3.2 — skills
        existing.setSkills(updatedProfile.getSkills());

        // expected graduation year
        existing.setExpectedGraduationYear(
                updatedProfile.getExpectedGraduationYear());

        return studentProfileRepository.save(existing);
    }

    // ─────────────────────────────────────────
    // FR3.3 — Toggle seeking mentorship
    // ─────────────────────────────────────────
    @Transactional
    public StudentProfile toggleMentorship(Long userId) {
        StudentProfile profile = getProfileByUserId(userId);
        // toggle current value
        profile.setIsMentee(!profile.getIsMentee());
        return studentProfileRepository.save(profile);
    }

    // ─────────────────────────────────────────
    // FR3.3 — Get all students seeking mentorship
    // ─────────────────────────────────────────
    public List<StudentProfile> getAllMentees() {
        return studentProfileRepository.findByIsMentee(true);
    }

    // ─────────────────────────────────────────
    // FR4.1, FR13.1 — Search student profiles
    // FR13.2 — partial keyword matching
    // ─────────────────────────────────────────
    public List<StudentProfile> searchProfiles(
            String  keyword,
            Boolean isMentee,
            Integer expectedGraduationYear) {
        return studentProfileRepository.searchStudentProfiles(
                keyword,
                isMentee,
                expectedGraduationYear);
    }

    // ─────────────────────────────────────────
    // FR3 — Delete student profile
    // ─────────────────────────────────────────
    @Transactional
    public void deleteProfile(Long userId) {
        StudentProfile profile = getProfileByUserId(userId);
        studentProfileRepository.delete(profile);
    }
}