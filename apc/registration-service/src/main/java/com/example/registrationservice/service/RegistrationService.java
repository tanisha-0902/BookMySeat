package com.example.registrationservice.service;

import com.example.registrationservice.dto.EventDto;
import com.example.registrationservice.model.Registration;
import com.example.registrationservice.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final RestTemplate restTemplate;
    private final String eventServiceUrl;

    public RegistrationService(RegistrationRepository registrationRepository,
                               RestTemplate restTemplate,
                               @Value("${event.service.url}") String eventServiceUrl) {
        this.registrationRepository = registrationRepository;
        this.restTemplate = restTemplate;
        this.eventServiceUrl = eventServiceUrl;
    }

    public Registration createRegistration(Long userId, Long eventId) {
        // STEP 1: Check if the event has already passed
        EventDto event = restTemplate.getForObject(eventServiceUrl + "/api/events/" + eventId, EventDto.class);
        if (event == null || event.getDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException("Cannot register for an event that has already passed.");
        }

        // STEP 2: Check if the user is already registered
        if (registrationRepository.existsByUserIdAndEventId(userId, eventId)) {
            throw new IllegalStateException("User is already registered for this event.");
        }

        // STEP 3: Proceed with registration
        return registrationRepository.save(new Registration(userId, eventId));
    }

    public List<Long> getEventIdsByUserId(Long userId) {
        return registrationRepository.findByUserId(userId)
                .stream()
                .map(Registration::getEventId)
                .collect(Collectors.toList());
    }

    public long getRegistrationCountForEvent(Long eventId) {
        return registrationRepository.countByEventId(eventId);
    }

    public Map<Long, Long> getRegistrationCountsForEvents(List<Long> eventIds) {
        if (eventIds == null || eventIds.isEmpty()) {
            return Map.of();
        }
        return registrationRepository.countByEventIdIn(eventIds)
                .stream()
                .collect(Collectors.toMap(
                        result -> (Long) result[0], // eventId
                        result -> (Long) result[1]  // count
                ));
    }

    public void cancelRegistration(Long userId, Long eventId) {
        registrationRepository.deleteByUserIdAndEventId(userId, eventId);
    }

    public List<Long> getUserIdsByEventId(Long eventId) {
        return registrationRepository.findByEventId(eventId)
                .stream()
                .map(Registration::getUserId)
                .collect(Collectors.toList());
    }
}