package com.example.registrationservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "registrations", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userId", "eventId"})
})
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long eventId;

    public Registration(Long userId, Long eventId) {
        this.userId = userId;
        this.eventId = eventId;
    }
}