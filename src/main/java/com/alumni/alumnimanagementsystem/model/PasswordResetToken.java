package com.alumni.alumnimanagementsystem.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FR15.2 — linked to user who requested reset
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // FR15.3 — unique single use token
    @Column(nullable = false, unique = true)
    private String token;

    // FR15.3 — time limited token
    // expires after set time (24 hours)
    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    // FR15.3 — track if token has been used
    @Column(name = "is_used")
    private Boolean isUsed = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // FR15.3 — check if token is expired
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }

    // FR15.3 — check if token is valid
    // must not be used and not expired
    public boolean isValid() {
        return !isUsed && !isExpired();
    }

    // -------- Getters & Setters --------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public LocalDateTime getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Boolean getIsUsed() { return isUsed; }
    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}