package com.example.userservice.dto;

import lombok.Data;
import java.time.LocalDate;
// import java.util.Set; // This is no longer needed

@Data
public class EventDto {
    private Long id;
    private String name;
    private LocalDate date;
    private String location;
    private String description;

    // This field is removed because the event-service no longer provides it.
    // private Set<Long> attendeeUserIds;
}