package com.alumni.alumnimanagementsystem.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alumni_profiles")
public class AlumniProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FR2 — linked to users table (one to one)
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // FR2.1 — graduation year
    @Column(name = "graduation_year")
    private Integer graduationYear;

    // FR2.3 — industry
    private String industry;

    // FR2.3 — location
    private String location;

    // FR2.1 — work history (company, title, dates)
    @Column(name = "work_history", columnDefinition = "TEXT")
    private String workHistory;

    // FR2.2 — skills (comma separated)
    @Column(columnDefinition = "TEXT")
    private String skills;

    // FR2.3 — bio
    @Column(columnDefinition = "TEXT")
    private String bio;

    // FR2.4 — available for mentorship
    @Column(name = "is_mentor")
    private Boolean isMentor = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // -------- Getters & Setters --------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Integer getGraduationYear() { return graduationYear; }
    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }

    public String getIndustry() { return industry; }
    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getLocation() { return location; }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getWorkHistory() { return workHistory; }
    public void setWorkHistory(String workHistory) {
        this.workHistory = workHistory;
    }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public Boolean getIsMentor() { return isMentor; }
    public void setIsMentor(Boolean isMentor) {
        this.isMentor = isMentor;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}