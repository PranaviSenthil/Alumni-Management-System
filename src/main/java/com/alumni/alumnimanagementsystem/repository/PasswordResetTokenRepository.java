package com.alumni.alumnimanagementsystem.repository;

import com.alumni.alumnimanagementsystem.model.PasswordResetToken;
import com.alumni.alumnimanagementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository
        extends JpaRepository<PasswordResetToken, Long> {

    // FR15.3 — find token by token string
    // used to validate reset link
    Optional<PasswordResetToken> findByToken(String token);

    // FR15.2 — find all tokens for a user
    // used to invalidate old tokens
    List<PasswordResetToken> findByUser(User user);

    // FR15.3 — find valid token for user
    // must not be used and not expired
    @Query("SELECT t FROM PasswordResetToken t WHERE " +
           "t.user = :user " +
           "AND t.isUsed = false " +
           "AND t.expiryDate > :now")
    Optional<PasswordResetToken> findValidTokenForUser(
            @Param("user") User user,
            @Param("now")  LocalDateTime now);

    // FR15.3 — find token by string and check validity
    // single use + not expired
    @Query("SELECT t FROM PasswordResetToken t WHERE " +
           "t.token = :token " +
           "AND t.isUsed = false " +
           "AND t.expiryDate > :now")
    Optional<PasswordResetToken> findValidToken(
            @Param("token") String token,
            @Param("now")   LocalDateTime now);

    // FR15.3 — delete all expired tokens
    // cleanup job to keep DB clean
    @Modifying
    @Transactional
    @Query("DELETE FROM PasswordResetToken t WHERE " +
           "t.expiryDate < :now OR t.isUsed = true")
    void deleteExpiredTokens(
            @Param("now") LocalDateTime now);

    // FR15.3 — delete all tokens for a user
    // when user resets password, invalidate all old tokens
    @Modifying
    @Transactional
    @Query("DELETE FROM PasswordResetToken t WHERE " +
           "t.user = :user")
    void deleteAllTokensForUser(
            @Param("user") User user);
}