package org.example.dto;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String name,
        String email,
        String phone,
        String role,
        Long patientId,
        Long medicalStaffId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
