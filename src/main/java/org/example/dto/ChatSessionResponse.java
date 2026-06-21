package org.example.dto;

import java.time.LocalDateTime;

public record ChatSessionResponse(
        Long id,
        Long patientId,
        String patientName,
        Long assignedMedicalStaffId,
        String assignedMedicalStaffName,
        String status,
        boolean escalationRequired,
        LocalDateTime closedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
