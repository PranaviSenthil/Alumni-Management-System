package com.alumni.alumnimanagementsystem.controller;

import com.alumni.alumnimanagementsystem.model.Message;
import com.alumni.alumnimanagementsystem.service.MessageService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/messages")

public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    
    // Send private message (connections only)
    
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(
            @RequestParam Long senderId,
            @RequestParam Long receiverId,
            @RequestParam @NotBlank String content) {
        try {
            Message message =
                    messageService.sendMessage(senderId, receiverId, content);
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

   
    // Message history between two users, ordered by time
    
    @GetMapping("/history")
    public ResponseEntity<?> getMessageHistory(
            @RequestParam Long userId1,
            @RequestParam Long userId2) {
        try {
            List<Message> history =
                    messageService.getMessageHistory(userId1, userId2);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    
    // FR14.1 — GET /api/v1/messages/{userId}/all
    
    @GetMapping("/{userId}/all")
    public ResponseEntity<?> getAllMessages(@PathVariable Long userId) {
        try {
            List<Message> messages =
                    messageService.getAllMessagesForUser(userId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    
    // FR14.1 — GET /api/v1/messages/{userId}/received
    
    @GetMapping("/{userId}/received")
    public ResponseEntity<?> getReceivedMessages(@PathVariable Long userId) {
        try {
            List<Message> messages =
                    messageService.getReceivedMessages(userId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // ─────────────────────────────────────────
    // FR14.1 — GET /api/v1/messages/{userId}/sent
    // ─────────────────────────────────────────
    @GetMapping("/{userId}/sent")
    public ResponseEntity<?> getSentMessages(@PathVariable Long userId) {
        try {
            List<Message> messages =
                    messageService.getSentMessages(userId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // ─────────────────────────────────────────
    // FR10.1 — GET /api/v1/messages/{userId}/count
    // for dashboard notification feed
    // ─────────────────────────────────────────
    @GetMapping("/{userId}/count")
    public ResponseEntity<?> getMessageCount(@PathVariable Long userId) {
        try {
            Long count = messageService.getMessageCount(userId);
            Map<String, Long> response = new HashMap<>();
            response.put("count", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}