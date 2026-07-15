package com.alumni.alumnimanagementsystem.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "connections")
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FR5.1 — user who sent the request
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    // FR5.1 — user who received the request
    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    // FR5.3, FR5.4 — connection status
    // PENDING → ACCEPTED or REJECTED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConnectionStatus status = ConnectionStatus.PENDING;

    // FR5.1 — when request was sent
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // FR5.4 — when status was last updated
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

    // ─────────────────────────────────────────
    // FR5.3 — Connection Status Enum
    // PENDING  → request sent, not yet responded
    // ACCEPTED → FR6.1 appears in connections list
    // REJECTED → FR5.3 request was rejected
    // ─────────────────────────────────────────
    public enum ConnectionStatus {
        PENDING,
        ACCEPTED,
        REJECTED
    }

    // -------- Getters & Setters --------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getSender() { return sender; }
    public void setSender(User sender) { this.sender = sender; }

    public User getReceiver() { return receiver; }
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public ConnectionStatus getStatus() { return status; }
    public void setStatus(ConnectionStatus status) {
        this.status = status;
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