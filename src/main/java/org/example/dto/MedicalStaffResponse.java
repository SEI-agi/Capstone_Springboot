package org.example.dto;

import java.time.LocalDateTime;

public record MedicalStaffResponse(
        Long id,
        Long userId,
        String userName,
        String userEmail,
        String firstName,
        String lastName,
        String licenseNumber,
        String expertise,
        String gender,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
