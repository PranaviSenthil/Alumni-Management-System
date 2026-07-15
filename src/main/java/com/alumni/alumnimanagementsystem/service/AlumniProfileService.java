package com.alumni.alumnimanagementsystem.service;

import com.alumni.alumnimanagementsystem.exception.ResourceNotFoundException;
import com.alumni.alumnimanagementsystem.model.AlumniProfile;
import com.alumni.alumnimanagementsystem.model.User;
import com.alumni.alumnimanagementsystem.repository.AlumniProfileRepository;
import com.alumni.alumnimanagementsystem.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlumniProfileService {

    private final AlumniProfileRepository alumniProfileRepository;
    private final UserRepository userRepository;

    public AlumniProfileService(
            AlumniProfileRepository alumniProfileRepository,
            UserRepository userRepository) {
        this.alumniProfileRepository = alumniProfileRepository;
        this.userRepository          = userRepository;
    }

    // ─────────────────────────────────────────
    // FR2 — Create alumni profile
    // called after user registers as ALUMNUS
    // ─────────────────────────────────────────
    @Transactional
    public AlumniProfile createProfile(
            Long userId, AlumniProfile profile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));

        // make sure user is ALUMNUS
        if (user.getUserType() != User.UserType.ALUMNUS) {
            throw new IllegalArgumentException(
                    "User is not an Alumnus");
        }

        profile.setUser(user);
        return alumniProfileRepository.save(profile);
    }

    // ─────────────────────────────────────────
    // FR2 — Get alumni profile by user id
    // ─────────────────────────────────────────
    public AlumniProfile getProfileByUserId(Long userId) {
        return alumniProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Alumni profile not found for user id: "
                                + userId));
    }

    // ─────────────────────────────────────────
    // FR2 — Get all alumni profiles
    // ─────────────────────────────────────────
    public List<AlumniProfile> getAllProfiles() {
        return alumniProfileRepository.findAll();
    }

    // ─────────────────────────────────────────
    // FR2.1 — Update work history
    // FR2.2 — Update skills
    // FR2.3 — Update industry, location, bio
    // ─────────────────────────────────────────
    @Transactional
    public AlumniProfile updateProfile(
            Long userId, AlumniProfile updatedProfile) {
        AlumniProfile existing = getProfileByUserId(userId);

        // FR2.1 — work history
        existing.setWorkHistory(updatedProfile.getWorkHistory());

        // FR2.2 — skills
        existing.setSkills(updatedProfile.getSkills());

        // FR2.3 — industry, location, bio
        existing.setIndustry(updatedProfile.getIndustry());
        existing.setLocation(updatedProfile.getLocation());
        existing.setBio(updatedProfile.getBio());

        // graduation year
        existing.setGraduationYear(
                updatedProfile.getGraduationYear());

        return alumniProfileRepository.save(existing);
    }

    // ─────────────────────────────────────────
    // FR2.4 — Toggle mentorship availability
    // ─────────────────────────────────────────
    @Transactional
    public AlumniProfile toggleMentorship(Long userId) {
        AlumniProfile profile = getProfileByUserId(userId);
        // toggle current value
        profile.setIsMentor(!profile.getIsMentor());
        return alumniProfileRepository.save(profile);
    }

    // ─────────────────────────────────────────
    // FR2.4 — Get all mentors
    // ─────────────────────────────────────────
    public List<AlumniProfile> getAllMentors() {
        return alumniProfileRepository.findByIsMentor(true);
    }

    // ─────────────────────────────────────────
    // FR4.1, FR13.1 — Search alumni profiles
    // FR13.2 — partial keyword matching
    // ─────────────────────────────────────────
    public List<AlumniProfile> searchProfiles(
            String  keyword,
            String  industry,
            String  location,
            Boolean isMentor,
            Integer graduationYear) {
        return alumniProfileRepository.searchAlumniProfiles(
                keyword,
                industry,
                location,
                isMentor,
                graduationYear);
    }

    // ─────────────────────────────────────────
    // FR2 — Delete alumni profile
    // ─────────────────────────────────────────
    @Transactional
    public void deleteProfile(Long userId) {
        AlumniProfile profile = getProfileByUserId(userId);
        alumniProfileRepository.delete(profile);
    }
}