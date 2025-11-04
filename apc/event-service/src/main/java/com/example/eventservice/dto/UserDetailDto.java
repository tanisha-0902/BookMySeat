package com.example.eventservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor; // 1. Import this annotation

// This DTO is used to deserialize the response from the user-service
@Data
@NoArgsConstructor // 2. Add this annotation
public class UserDetailDto {
    private Long id;
    private String username;
}