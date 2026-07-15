package com.alumni.alumnimanagementsystem.repository;

import com.alumni.alumnimanagementsystem.model.AlumniProfile;
import com.alumni.alumnimanagementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlumniProfileRepository
        extends JpaRepository<AlumniProfile, Long> {

    // FR2 — find profile by user
    Optional<AlumniProfile> findByUser(User user);

    // FR2 — find profile by user id
    Optional<AlumniProfile> findByUserId(Long userId);

    // FR2.4 — find all mentors
    List<AlumniProfile> findByIsMentor(Boolean isMentor);

    // FR4.1, FR13.1 — search alumni profiles
    // by name, industry, location, skills, graduation year
    // FR13.2 — supports partial keyword matching
    @Query("SELECT a FROM AlumniProfile a WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "   LOWER(a.user.firstName) LIKE LOWER(CONCAT('%',:keyword,'%')) OR " +
           "   LOWER(a.user.lastName)  LIKE LOWER(CONCAT('%',:keyword,'%')) OR " +
           "   LOWER(a.industry)       LIKE LOWER(CONCAT('%',:keyword,'%')) OR " +
           "   LOWER(a.location)       LIKE LOWER(CONCAT('%',:keyword,'%')) OR " +
           "   LOWER(a.skills)         LIKE LOWER(CONCAT('%',:keyword,'%')) OR " +
           "   LOWER(a.workHistory)    LIKE LOWER(CONCAT('%',:keyword,'%'))) " +
           "AND (:industry IS NULL OR " +
           "   LOWER(a.industry) = LOWER(:industry)) " +
           "AND (:location IS NULL OR " +
           "   LOWER(a.location) = LOWER(:location)) " +
           "AND (:isMentor IS NULL OR " +
           "   a.isMentor = :isMentor) " +
           "AND (:graduationYear IS NULL OR " +
           "   a.graduationYear = :graduationYear) " +
           "AND a.user.isActive = true")
    List<AlumniProfile> searchAlumniProfiles(
            @Param("keyword")        String  keyword,
            @Param("industry")       String  industry,
            @Param("location")       String  location,
            @Param("isMentor")       Boolean isMentor,
            @Param("graduationYear") Integer graduationYear
    );
}