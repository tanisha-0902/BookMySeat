package com.example.userservice.dto;

import lombok.Data;

/**
 * This is a Data Transfer Object (DTO) used by the user-service.
 * Its purpose is to represent a registration record received from the registration-service API.
 * It's a simple container for the data.
 */
@Data
public class RegistrationDto {

    // The unique ID of the registration itself
    private Long id;

    // The ID of the user who is registered
    private Long userId;

    // The ID of the event the user is registered for
    private Long eventId;
}