package com.alumni.alumnimanagementsystem.repository;
import com.alumni.alumnimanagementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // FR1.4 — find user by email for login/JWT
    Optional<User> findByEmail(String email);

    // FR1.5 — check email uniqueness before registration
    boolean existsByEmail(String email);

    // FR11.2 — filter users by type (ALUMNUS/STUDENT) for admin
    List<User> findByUserType(User.UserType userType);

    // FR11.2 — search by name or email for admin dashboard
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.lastName)  LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.email)     LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> searchByKeyword(@Param("keyword") String keyword);

    // FR9.1 — find all active users
    List<User> findByIsActive(Boolean isActive);

    // FR12 — find all unverified users for admin verification
    List<User> findByIsVerified(Boolean isVerified);
}