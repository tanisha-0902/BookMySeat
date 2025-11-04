package com.example.registrationservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class EventDto {
    private Long id;
    private String name;
    private LocalDate date;
    private String location;
    private String description;
}