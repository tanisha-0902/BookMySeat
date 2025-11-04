package com.example.userservice.controller;

import com.example.userservice.dto.EventDto;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class UserDashboardController {

    private final RestTemplate restTemplate;
    private final String eventServiceUrl;
    private final String registrationServiceUrl;
    private final UserRepository userRepository;

    public UserDashboardController(RestTemplate restTemplate,
                                   @Value("${event.service.url}") String eventServiceUrl,
                                   @Value("${registration.service.url}") String registrationServiceUrl,
                                   UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.eventServiceUrl = eventServiceUrl;
        this.registrationServiceUrl = registrationServiceUrl;
        this.userRepository = userRepository;
    }

    @GetMapping("/dashboard")
    public String userDashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("Current user not found."));

        // STEP 1: Get all events from the event-service
        String eventsUrl = eventServiceUrl + "/api/events";
        ResponseEntity<List<EventDto>> eventsResponse = restTemplate.exchange(
                eventsUrl, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        List<EventDto> events = eventsResponse.getBody();

        // STEP 2: Get the IDs of events this user is registered for from the registration-service
        String regsUrl = registrationServiceUrl + "/api/registrations/user/" + currentUser.getId();
        ResponseEntity<List<Long>> regsResponse = restTemplate.exchange(
                regsUrl, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        Set<Long> registeredEventIds = new HashSet<>();
        if (regsResponse.getBody() != null) {
            registeredEventIds.addAll(regsResponse.getBody());
        }

        // Add username for the welcome message
        model.addAttribute("username", userDetails.getUsername());

        // Add count of registered events for the hero banner
        model.addAttribute("registeredEventsCount", registeredEventIds.size());

        model.addAttribute("events", events != null ? events : Collections.emptyList());
        model.addAttribute("userId", currentUser.getId());
        model.addAttribute("registeredEventIds", registeredEventIds);
        return "user-dashboard";
    }

    @PostMapping("/dashboard/register/{eventId}")
    public String registerForEvent(@PathVariable Long eventId, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cannot find logged in user details."));

        // Make an API call to the registration-service to register the user.
        String url = registrationServiceUrl + "/api/registrations";
        try {
            // The payload for the registration service requires both the user's ID and the event's ID.
            Map<String, Long> payload = Map.of("userId", currentUser.getId(), "eventId", eventId);
            restTemplate.postForEntity(url, payload, String.class);
        } catch (Exception e) {
            System.err.println("Error registering for event: " + e.getMessage());
            // In a real application, you would add a flash attribute here
            // to show an error message on the user interface.
        }

        return "redirect:/dashboard";
    }

    @PostMapping("/dashboard/cancel/{eventId}")
    public String cancelRegistration(@PathVariable Long eventId, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("Cannot find logged in user details."));

        // Build the URL with query parameters
        String url = UriComponentsBuilder.fromHttpUrl(registrationServiceUrl + "/api/registrations")
                .queryParam("userId", currentUser.getId())
                .queryParam("eventId", eventId)
                .toUriString();

        try {
            // Make an HTTP DELETE request to the registration service
            restTemplate.delete(url);
        } catch (Exception e) {
            System.err.println("Error cancelling registration: " + e.getMessage());
        }

        return "redirect:/dashboard";
    }
}