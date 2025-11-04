package com.example.registrationservice.repository;

import com.example.registrationservice.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    // Find all events a specific user is registered for
    List<Registration> findByUserId(Long userId);

    // Count how many users are registered for a specific event
    long countByEventId(Long eventId);

    // Check if a specific registration already exists
    boolean existsByUserIdAndEventId(Long userId, Long eventId);

    @Query("SELECT r.eventId, COUNT(r.id) FROM Registration r WHERE r.eventId IN :eventIds GROUP BY r.eventId")
    List<Object[]> countByEventIdIn(@Param("eventIds") List<Long> eventIds);

    @Transactional
    void deleteByUserIdAndEventId(Long userId, Long eventId);

    // NEW: Method to find all registrations for an event
    List<Registration> findByEventId(Long eventId);
}