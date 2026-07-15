package com.alumni.alumnimanagementsystem.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_profiles")
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FR3 — linked to users table (one to one)
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // FR3.1 — expected graduation year
    @Column(name = "expected_graduation_year")
    private Integer expectedGraduationYear;

    // FR3.1 — academic interests
    @Column(name = "academic_interests", columnDefinition = "TEXT")
    private String academicInterests;

    // FR3.1 — career goals
    @Column(name = "career_goals", columnDefinition = "TEXT")
    private String careerGoals;

    // FR3.2 — skills (comma separated)
    @Column(columnDefinition = "TEXT")
    private String skills;

    // FR3.1 — bio
    @Column(columnDefinition = "TEXT")
    private String bio;

    // FR3.3 — seeking mentorship
    @Column(name = "is_mentee")
    private Boolean isMentee = false;

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

    public Integer getExpectedGraduationYear() {
        return expectedGraduationYear;
    }
    public void setExpectedGraduationYear(Integer expectedGraduationYear) {
        this.expectedGraduationYear = expectedGraduationYear;
    }

    public String getAcademicInterests() { return academicInterests; }
    public void setAcademicInterests(String academicInterests) {
        this.academicInterests = academicInterests;
    }

    public String getCareerGoals() { return careerGoals; }
    public void setCareerGoals(String careerGoals) {
        this.careerGoals = careerGoals;
    }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public Boolean getIsMentee() { return isMentee; }
    public void setIsMentee(Boolean isMentee) {
        this.isMentee = isMentee;
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