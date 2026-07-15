package com.alumni.alumnimanagementsystem.service;

import com.alumni.alumnimanagementsystem.exception.ResourceNotFoundException;
import com.alumni.alumnimanagementsystem.model.Connection;
import com.alumni.alumnimanagementsystem.model.Connection.ConnectionStatus;
import com.alumni.alumnimanagementsystem.model.User;
import com.alumni.alumnimanagementsystem.repository.ConnectionRepository;
import com.alumni.alumnimanagementsystem.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final UserRepository userRepository;

    public ConnectionService(
            ConnectionRepository connectionRepository,
            UserRepository userRepository) {
        this.connectionRepository = connectionRepository;
        this.userRepository       = userRepository;
    }

    // ─────────────────────────────────────────
    // FR5.1 — Send connection request
    // FR5.2 — creates PENDING notification
    // ─────────────────────────────────────────
    @Transactional
    public Connection sendConnectionRequest(
            Long senderId, Long receiverId) {

        // get sender and receiver
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sender not found with id: " + senderId));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Receiver not found with id: " + receiverId));

        // cannot send request to yourself
        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException(
                    "Cannot send connection request to yourself");
        }

        // FR5.1 — check if connection already exists
        connectionRepository
                .findConnectionBetweenUsers(sender, receiver)
                .ifPresent(c -> {
                    throw new IllegalArgumentException(
                            "Connection already exists between users");
                });

        // FR5.2 — create PENDING connection
        Connection connection = new Connection();
        connection.setSender(sender);
        connection.setReceiver(receiver);
        connection.setStatus(ConnectionStatus.PENDING);

        return connectionRepository.save(connection);
    }

    // ─────────────────────────────────────────
    // FR5.3 — Accept connection request
    // FR5.4 — status updated to ACCEPTED in DB
    // ─────────────────────────────────────────
    @Transactional
    public Connection acceptConnection(
            Long connectionId, Long receiverId) {

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + receiverId));

        // FR5.3 — only receiver can accept
        Connection connection = connectionRepository
                .findByIdAndReceiver(connectionId, receiver)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Connection request not found"));

        // FR5.4 — update status to ACCEPTED
        connection.setStatus(ConnectionStatus.ACCEPTED);
        return connectionRepository.save(connection);
    }

    // ─────────────────────────────────────────
    // FR5.3 — Reject connection request
    // FR5.4 — status updated to REJECTED in DB
    // ─────────────────────────────────────────
    @Transactional
    public Connection rejectConnection(
            Long connectionId, Long receiverId) {

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + receiverId));

        // FR5.3 — only receiver can reject
        Connection connection = connectionRepository
                .findByIdAndReceiver(connectionId, receiver)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Connection request not found"));

        // FR5.4 — update status to REJECTED
        connection.setStatus(ConnectionStatus.REJECTED);
        return connectionRepository.save(connection);
    }

    // ─────────────────────────────────────────
    // FR6.1 — Get all accepted connections
    // ─────────────────────────────────────────
    public List<Connection> getAcceptedConnections(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        return connectionRepository.findAcceptedConnections(user);
    }

    // ─────────────────────────────────────────
    // FR6.2 — Get pending requests received
    // FR5.2 — for notification feed
    // ─────────────────────────────────────────
    public List<Connection> getPendingRequestsReceived(
            Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        return connectionRepository
                .findPendingRequestsReceived(user);
    }

    // ─────────────────────────────────────────
    // FR6.2 — Get pending requests sent
    // ─────────────────────────────────────────
    public List<Connection> getPendingRequestsSent(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        return connectionRepository
                .findPendingRequestsSent(user);
    }

    // ─────────────────────────────────────────
    // FR6.4 — Disconnect from a connection
    // ─────────────────────────────────────────
    @Transactional
    public void disconnect(Long connectionId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));

        Connection connection = connectionRepository
                .findById(connectionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Connection not found with id: "
                                + connectionId));

        // FR6.4 — only sender or receiver can disconnect
        if (!connection.getSender().getId().equals(userId) &&
            !connection.getReceiver().getId().equals(userId)) {
            throw new IllegalArgumentException(
                    "Not authorized to disconnect this connection");
        }

        connectionRepository.delete(connection);
    }

    // ─────────────────────────────────────────
    // FR6.3 — Get all connections for a user
    // ─────────────────────────────────────────
    public List<Connection> getAllConnections(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        return connectionRepository
                .findAllConnectionsForUser(user);
    }
}