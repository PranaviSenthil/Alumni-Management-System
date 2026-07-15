package com.alumni.alumnimanagementsystem.service;

import com.alumni.alumnimanagementsystem.exception.ResourceNotFoundException;
import com.alumni.alumnimanagementsystem.model.Connection.ConnectionStatus;
import com.alumni.alumnimanagementsystem.model.Message;
import com.alumni.alumnimanagementsystem.model.User;
import com.alumni.alumnimanagementsystem.repository.ConnectionRepository;
import com.alumni.alumnimanagementsystem.repository.MessageRepository;
import com.alumni.alumnimanagementsystem.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository    messageRepository;
    private final UserRepository       userRepository;
    private final ConnectionRepository connectionRepository;

    public MessageService(
            MessageRepository    messageRepository,
            UserRepository       userRepository,
            ConnectionRepository connectionRepository) {
        this.messageRepository    = messageRepository;
        this.userRepository       = userRepository;
        this.connectionRepository = connectionRepository;
    }

    // ─────────────────────────────────────────
    // FR14.1 — Send private message
    // only allowed between accepted connections
    // FR14.3 — text only, no file attachments
    // ─────────────────────────────────────────
    @Transactional
    public Message sendMessage(
            Long senderId,
            Long receiverId,
            String content) {

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sender not found with id: " + senderId));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Receiver not found with id: " + receiverId));

        // FR14.1 — can only message accepted connections
        connectionRepository
                .findConnectionBetweenUsers(sender, receiver)
                .ifPresentOrElse(
                        connection -> {
                            if (connection.getStatus()
                                    != ConnectionStatus.ACCEPTED) {
                                throw new IllegalArgumentException(
                                        "Users are not connected");
                            }
                        },
                        () -> {
                            throw new IllegalArgumentException(
                                    "Users are not connected");
                        });

        // FR14.3 — text only message
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);

        return messageRepository.save(message);
    }

    // ─────────────────────────────────────────
    // FR14.2 — Get message history
    // between two users ordered by time
    // ─────────────────────────────────────────
    public List<Message> getMessageHistory(
            Long userId1, Long userId2) {

        User user1 = userRepository.findById(userId1)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId1));

        User user2 = userRepository.findById(userId2)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId2));

        // FR14.2 — returns messages ordered by createdAt ASC
        return messageRepository
                .findMessagesBetweenUsers(user1, user2);
    }

    // ─────────────────────────────────────────
    // FR14.1 — Get all messages for a user
    // ─────────────────────────────────────────
    public List<Message> getAllMessagesForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        return messageRepository.findAllMessagesForUser(user);
    }

    // ─────────────────────────────────────────
    // FR14.1 — Get received messages
    // ─────────────────────────────────────────
    public List<Message> getReceivedMessages(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        return messageRepository
                .findByReceiverOrderByCreatedAtDesc(user);
    }

    // ─────────────────────────────────────────
    // FR14.1 — Get sent messages
    // ─────────────────────────────────────────
    public List<Message> getSentMessages(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        return messageRepository
                .findBySenderOrderByCreatedAtDesc(user);
    }

    // ─────────────────────────────────────────
    // FR10.1 — Get message count for dashboard
    // notification feed
    // ─────────────────────────────────────────
    public Long getMessageCount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
        return messageRepository.countMessagesForUser(user);
    }
}