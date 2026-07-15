package com.alumni.alumnimanagementsystem.controller;

import com.alumni.alumnimanagementsystem.model.Connection;
import com.alumni.alumnimanagementsystem.service.ConnectionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/connections")

public class ConnectionController {

    private final ConnectionService connectionService;

    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    // ─────────────────────────────────────────
    // FR5.1 — POST /api/v1/connections/request
    // Send connection request
    // FR5.2 — creates PENDING notification
    // ─────────────────────────────────────────
    @PostMapping("/request")
    public ResponseEntity<?> sendRequest(
            @RequestParam Long senderId,
            @RequestParam Long receiverId) {
        try {
            Connection connection =
                    connectionService.sendConnectionRequest(senderId, receiverId);
            return ResponseEntity.status(HttpStatus.CREATED).body(connection);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // ─────────────────────────────────────────
    // FR5.3 — PUT /api/v1/connections/{id}/accept
    // FR5.4 — status updated to ACCEPTED
    // ─────────────────────────────────────────
    @PutMapping("/{id}/accept")
    public ResponseEntity<?> acceptConnection(
            @PathVariable Long id,
            @RequestParam Long receiverId) {
        try {
            Connection connection =
                    connectionService.acceptConnection(id, receiverId);
            return ResponseEntity.ok(connection);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // ─────────────────────────────────────────
    // FR5.3 — PUT /api/v1/connections/{id}/reject
    // FR5.4 — status updated to REJECTED
    // ─────────────────────────────────────────
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectConnection(
            @PathVariable Long id,
            @RequestParam Long receiverId) {
        try {
            Connection connection =
                    connectionService.rejectConnection(id, receiverId);
            return ResponseEntity.ok(connection);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // ─────────────────────────────────────────
    // FR6.1 — GET /api/v1/connections/{userId}/accepted
    // ─────────────────────────────────────────
    @GetMapping("/{userId}/accepted")
    public ResponseEntity<?> getAcceptedConnections(
            @PathVariable Long userId) {
        try {
            List<Connection> connections =
                    connectionService.getAcceptedConnections(userId);
            return ResponseEntity.ok(connections);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // ─────────────────────────────────────────
    // FR6.2, FR5.2 — GET /api/v1/connections/{userId}/pending-received
    // for notification feed
    // ─────────────────────────────────────────
    @GetMapping("/{userId}/pending-received")
    public ResponseEntity<?> getPendingReceived(
            @PathVariable Long userId) {
        try {
            List<Connection> connections =
                    connectionService.getPendingRequestsReceived(userId);
            return ResponseEntity.ok(connections);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // ─────────────────────────────────────────
    // FR6.2 — GET /api/v1/connections/{userId}/pending-sent
    // ─────────────────────────────────────────
    @GetMapping("/{userId}/pending-sent")
    public ResponseEntity<?> getPendingSent(
            @PathVariable Long userId) {
        try {
            List<Connection> connections =
                    connectionService.getPendingRequestsSent(userId);
            return ResponseEntity.ok(connections);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // ─────────────────────────────────────────
    // FR6.3 — GET /api/v1/connections/{userId}/all
    // ─────────────────────────────────────────
    @GetMapping("/{userId}/all")
    public ResponseEntity<?> getAllConnections(
            @PathVariable Long userId) {
        try {
            List<Connection> connections =
                    connectionService.getAllConnections(userId);
            return ResponseEntity.ok(connections);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // ─────────────────────────────────────────
    // FR6.4 — DELETE /api/v1/connections/{id}/disconnect
    // ─────────────────────────────────────────
    @DeleteMapping("/{id}/disconnect")
    public ResponseEntity<?> disconnect(
            @PathVariable Long id,
            @RequestParam Long userId) {
        try {
            connectionService.disconnect(id, userId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Disconnected successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}