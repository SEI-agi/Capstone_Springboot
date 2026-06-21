package org.example.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PatientResponse(
        Long id,
        Long userId,
        String userName,
        String userEmail,
        String nextOfKinName,
        String nextOfKinPhone,
        String medicalNotes,
        String prescriptionNotes,
        LocalDate startDate,
        LocalDate endDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
