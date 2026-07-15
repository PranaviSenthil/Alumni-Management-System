package com.alumni.alumnimanagementsystem.service;

import com.alumni.alumnimanagementsystem.exception.ResourceNotFoundException;
import com.alumni.alumnimanagementsystem.model.User;
import com.alumni.alumnimanagementsystem.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder) {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ─────────────────────────────────────────
    // FR1 — User Registration
    // FR1.3 — BCrypt hashing (work factor 12)
    // FR1.5 — Unique email check
    // ─────────────────────────────────────────
    @Transactional
    public User registerUser(User user) {
        // FR1.5 — email must be unique across all user types
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException(
                "Email already in use: " + user.getEmail()
            );
        }
        // FR1.3 — hash password with BCrypt work factor 12
        user.setPasswordHash(
            passwordEncoder.encode(user.getPasswordHash())
        );
        return userRepository.save(user);
    }

    // ─────────────────────────────────────────
    // FR11 — Get all users (Admin dashboard)
    // ─────────────────────────────────────────
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ─────────────────────────────────────────
    // FR11.3 — Get user by ID (Admin view profile)
    // ─────────────────────────────────────────
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "User not found with id: " + id
                ));
    }

    // ─────────────────────────────────────────
    // FR1.4 — Get user by email (for JWT login)
    // ─────────────────────────────────────────
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // ─────────────────────────────────────────
    // FR9.1 — Update user profile
    // ─────────────────────────────────────────
    @Transactional
    public User updateUser(Long id, User updatedUser) {
        User existing = getUserById(id);
        existing.setFirstName(updatedUser.getFirstName());
        existing.setLastName(updatedUser.getLastName());
        existing.setProfilePictureUrl(
            updatedUser.getProfilePictureUrl()
        );
        return userRepository.save(existing);
    }

    // ─────────────────────────────────────────
    // FR9.1 — Deactivate user (soft delete)
    // profile invisible in search but data kept
    // ─────────────────────────────────────────
    @Transactional
    public void deactivateUser(Long id) {
        User user = getUserById(id);
        user.setIsActive(false);
        userRepository.save(user);
    }

    // ─────────────────────────────────────────
    // FR9.2 — Permanently delete user
    // irreversible — requires confirmation
    // ─────────────────────────────────────────
    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    // ─────────────────────────────────────────
    // FR12 — Admin verifies a user account
    // FR12.2 — sets isVerified = true
    // ─────────────────────────────────────────
    @Transactional
    public User verifyUser(Long id) {
        User user = getUserById(id);
        user.setIsVerified(true);
        return userRepository.save(user);
    }

    // ─────────────────────────────────────────
    // FR11.2 — Admin search users by keyword
    // ─────────────────────────────────────────
    public List<User> searchUsers(String keyword) {
        return userRepository.searchByKeyword(keyword);
    }

    // ─────────────────────────────────────────
    // FR11.2 — Admin filter users by type
    // ─────────────────────────────────────────
    public List<User> getUsersByType(User.UserType userType) {
        return userRepository.findByUserType(userType);
    }
}