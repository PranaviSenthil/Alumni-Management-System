package com.alumni.alumnimanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FR14.1 — user who sent the message
    // must be connected to receiver (checked in service)
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    // FR14.1 — user who received the message
    // must be connected to sender (checked in service)
    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    // FR14.1 — message content (text only)
    // FR14.3 — no file attachments, just text
    @NotBlank(message = "Message content is required")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    // FR14.2 — when message was sent
    // used to show message history in order
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
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

    public String getContent() { return content; }
    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}