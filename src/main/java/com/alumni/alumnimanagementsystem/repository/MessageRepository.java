package com.alumni.alumnimanagementsystem.repository;

import com.alumni.alumnimanagementsystem.model.Message;
import com.alumni.alumnimanagementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository
        extends JpaRepository<Message, Long> {

    // FR14.2 — get message history between two users
    // ordered by createdAt ascending (oldest first)
    @Query("SELECT m FROM Message m WHERE " +
           "(m.sender = :user1 AND m.receiver = :user2) OR " +
           "(m.sender = :user2 AND m.receiver = :user1) " +
           "ORDER BY m.createdAt ASC")
    List<Message> findMessagesBetweenUsers(
            @Param("user1") User user1,
            @Param("user2") User user2);

    // FR14.1 — get all messages received by a user
    List<Message> findByReceiverOrderByCreatedAtDesc(
            User receiver);

    // FR14.1 — get all messages sent by a user
    List<Message> findBySenderOrderByCreatedAtDesc(
            User sender);

    // FR10.1 — get unread messages count for dashboard
    // notification feed
    @Query("SELECT COUNT(m) FROM Message m WHERE " +
           "m.receiver = :user")
    Long countMessagesForUser(
            @Param("user") User user);

    // FR14.2 — get recent conversations for a user
    // shows latest message from each conversation
    @Query("SELECT m FROM Message m WHERE " +
           "m.sender = :user OR m.receiver = :user " +
           "ORDER BY m.createdAt DESC")
    List<Message> findAllMessagesForUser(
            @Param("user") User user);
}
