package com.alumni.alumnimanagementsystem.repository;

import com.alumni.alumnimanagementsystem.model.Connection;
import com.alumni.alumnimanagementsystem.model.Connection.ConnectionStatus;
import com.alumni.alumnimanagementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRepository
        extends JpaRepository<Connection, Long> {

    // FR6.1 — get all accepted connections for a user
    // user can be either sender or receiver
    @Query("SELECT c FROM Connection c WHERE " +
           "(c.sender = :user OR c.receiver = :user) " +
           "AND c.status = 'ACCEPTED'")
    List<Connection> findAcceptedConnections(
            @Param("user") User user);

    // FR5.2, FR6.2 — get all pending requests
    // received by a user (for notifications)
    @Query("SELECT c FROM Connection c WHERE " +
           "c.receiver = :user " +
           "AND c.status = 'PENDING'")
    List<Connection> findPendingRequestsReceived(
            @Param("user") User user);

    // FR6.2 — get all pending requests
    // sent by a user
    @Query("SELECT c FROM Connection c WHERE " +
           "c.sender = :user " +
           "AND c.status = 'PENDING'")
    List<Connection> findPendingRequestsSent(
            @Param("user") User user);

    // FR5.1 — check if connection already exists
    // between two users in any direction
    @Query("SELECT c FROM Connection c WHERE " +
           "(c.sender = :user1 AND c.receiver = :user2) OR " +
           "(c.sender = :user2 AND c.receiver = :user1)")
    Optional<Connection> findConnectionBetweenUsers(
            @Param("user1") User user1,
            @Param("user2") User user2);

    // FR5.3 — find connection by id and receiver
    // only receiver can accept or reject
    Optional<Connection> findByIdAndReceiver(
            Long id, User receiver);

    // FR6.4 — find all connections for a user
    // both sent and received in any status
    @Query("SELECT c FROM Connection c WHERE " +
           "c.sender = :user OR c.receiver = :user")
    List<Connection> findAllConnectionsForUser(
            @Param("user") User user);

    // FR5.4 — find by status for a user
    @Query("SELECT c FROM Connection c WHERE " +
           "(c.sender = :user OR c.receiver = :user) " +
           "AND c.status = :status")
    List<Connection> findByUserAndStatus(
            @Param("user")   User             user,
            @Param("status") ConnectionStatus status);
}