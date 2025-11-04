package com.example.registrationservice.controller;

import com.example.registrationservice.model.Registration;
import com.example.registrationservice.service.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<?> createRegistration(@RequestBody Map<String, Long> payload) {
        try {
            Registration newRegistration = registrationService.createRegistration(payload.get("userId"), payload.get("eventId"));
            return ResponseEntity.ok(newRegistration);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public List<Long> getRegistrationsByUser(@PathVariable Long userId) {
        return registrationService.getEventIdsByUserId(userId);
    }

    @GetMapping("/event/{eventId}/count")
    public Map<String, Long> getRegistrationCount(@PathVariable Long eventId) {
        return Map.of("count", registrationService.getRegistrationCountForEvent(eventId));
    }

    @GetMapping("/counts")
    public Map<Long, Long> getRegistrationCounts(@RequestParam List<Long> eventIds) {
        return registrationService.getRegistrationCountsForEvents(eventIds);
    }

    @DeleteMapping
    public ResponseEntity<Void> cancelRegistration(@RequestParam Long userId, @RequestParam Long eventId) {
        registrationService.cancelRegistration(userId, eventId);
        return ResponseEntity.noContent().build();
    }

    // NEW: Endpoint to get all user IDs for an event
    @GetMapping("/event/{eventId}/users")
    public List<Long> getRegisteredUsersForEvent(@PathVariable Long eventId) {
        return registrationService.getUserIdsByEventId(eventId);
    }
}