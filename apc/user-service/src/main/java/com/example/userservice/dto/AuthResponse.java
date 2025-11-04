package com.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Collection;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String jwt;
    private Collection<String> roles;
}