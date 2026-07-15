package com.alumni.alumnimanagementsystem.repository;

import com.alumni.alumnimanagementsystem.model.StudentProfile;
import com.alumni.alumnimanagementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentProfileRepository
        extends JpaRepository<StudentProfile, Long> {

    // FR3 — find profile by user
    Optional<StudentProfile> findByUser(User user);

    // FR3 — find profile by user id
    Optional<StudentProfile> findByUserId(Long userId);

    // FR3.3 — find all students seeking mentorship
    List<StudentProfile> findByIsMentee(Boolean isMentee);

    // FR4.1, FR13.1 — search student profiles
    // by name, skills, career goals, academic interests
    // FR13.2 — supports partial keyword matching
    @Query("SELECT s FROM StudentProfile s WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "   LOWER(s.user.firstName)      LIKE LOWER(CONCAT('%',:keyword,'%')) OR " +
           "   LOWER(s.user.lastName)       LIKE LOWER(CONCAT('%',:keyword,'%')) OR " +
           "   LOWER(s.skills)              LIKE LOWER(CONCAT('%',:keyword,'%')) OR " +
           "   LOWER(s.careerGoals)         LIKE LOWER(CONCAT('%',:keyword,'%')) OR " +
           "   LOWER(s.academicInterests)   LIKE LOWER(CONCAT('%',:keyword,'%'))) " +
           "AND (:isMentee IS NULL OR " +
           "   s.isMentee = :isMentee) " +
           "AND (:expectedGraduationYear IS NULL OR " +
           "   s.expectedGraduationYear = :expectedGraduationYear) " +
           "AND s.user.isActive = true")
    List<StudentProfile> searchStudentProfiles(
            @Param("keyword")               String  keyword,
            @Param("isMentee")              Boolean isMentee,
            @Param("expectedGraduationYear") Integer expectedGraduationYear
    );
}