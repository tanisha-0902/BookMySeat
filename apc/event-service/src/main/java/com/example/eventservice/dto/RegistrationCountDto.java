package com.example.eventservice.dto;

import lombok.Data;

/**
 * This is a Data Transfer Object (DTO) used by the event-service.
 * Its only purpose is to deserialize the JSON response from the registration-service
 * when fetching the number of attendees for an event.
 * It expects a JSON structure like: { "count": 123 }
 */
@Data
public class RegistrationCountDto {

    /**
     * This field directly maps to the 'count' key in the JSON response
     * from the registration-service's count endpoint.
     */
    private long count;

}