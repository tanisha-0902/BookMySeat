package com.example.eventservice.controller;

import com.example.eventservice.dto.RegistrationCountDto;
import com.example.eventservice.dto.UserDetailDto;
import com.example.eventservice.model.Event;
import com.example.eventservice.service.EventService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final EventService eventService;
    private final RestTemplate restTemplate;
    private final String registrationServiceUrl;
    private final String userServiceUrl;

    public AdminDashboardController(EventService eventService, RestTemplate restTemplate,
                                    @Value("${registration.service.url}") String registrationServiceUrl,
                                    @Value("${user.service.url}") String userServiceUrl) {
        this.eventService = eventService;
        this.restTemplate = restTemplate;
        this.registrationServiceUrl = registrationServiceUrl;
        this.userServiceUrl = userServiceUrl;
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        List<Event> events = eventService.getAllEvents();
        Map<Long, Long> attendeeCounts = events.stream()
                .collect(Collectors.toMap(Event::getId, event -> getRegistrationCount(event.getId())));

        model.addAttribute("events", events);
        model.addAttribute("attendeeCounts", attendeeCounts);
        model.addAttribute("newEvent", new Event());
        return "admin-dashboard";
    }

    private long getRegistrationCount(Long eventId) {
        String url = registrationServiceUrl + "/api/registrations/event/" + eventId + "/count";
        try {
            RegistrationCountDto result = restTemplate.getForObject(url, RegistrationCountDto.class);
            return result != null ? result.getCount() : 0;
        } catch (Exception e) {
            System.err.println("Could not fetch registration count for event " + eventId + ": " + e.getMessage());
            return 0;
        }
    }

    @GetMapping("/events/details/{id}")
    public String eventDetails(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid event Id:" + id));
        model.addAttribute("event", event);

        List<Long> userIds = restTemplate.exchange(
                registrationServiceUrl + "/api/registrations/event/" + id + "/users",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Long>>() {}
        ).getBody();

        if (userIds != null && !userIds.isEmpty()) {
            String url = UriComponentsBuilder.fromHttpUrl(userServiceUrl + "/api/users")
                    .queryParam("ids", userIds)
                    .toUriString();

            List<UserDetailDto> registeredUsers = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<UserDetailDto>>() {}
            ).getBody();
            model.addAttribute("registeredUsers", registeredUsers);
        } else {
            model.addAttribute("registeredUsers", Collections.emptyList());
        }

        return "event-details";
    }

    @PostMapping("/events/create")
    public String createEvent(@ModelAttribute Event newEvent, RedirectAttributes redirectAttributes) {
        if (newEvent.getDate().isBefore(LocalDate.now())) {
            redirectAttributes.addFlashAttribute("error", "Event date cannot be in the past.");
            return "redirect:/admin/dashboard";
        }
        eventService.saveEvent(newEvent);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/events/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid event Id:" + id));
        model.addAttribute("event", event);
        return "update_event";
    }

    @PostMapping("/events/update/{id}")
    public String updateEvent(@PathVariable Long id, @ModelAttribute Event event, RedirectAttributes redirectAttributes) {
        if (event.getDate().isBefore(LocalDate.now())) {
            redirectAttributes.addFlashAttribute("error", "Event date cannot be in the past.");
            return "redirect:/admin/events/edit/" + id;
        }
        event.setId(id);
        eventService.saveEvent(event);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/events/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return "redirect:/admin/dashboard";
    }
}