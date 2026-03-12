package com.demo.ai.repository;

import com.demo.ai.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserJpaRepository extends JpaRepository<User, UUID> {
    // Custom Query for Permanent Delete
    @Modifying
    @Query("""
            DELETE FROM User u 
            WHERE u.deleted = true 
            AND u.deletedAt < :cutoff
            """)
    void deleteUsersPermanently(Instant cutoff);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByUserIdAndDeletedTrue(UUID userId);
}
