package org.example.dto;

import java.time.LocalDateTime;

public record SubscriptionResponse(
        Long id,
        Long patientId,
        String patientName,
        Long packageId,
        String packageName,
        Long medicalStaffId,
        String medicalStaffName,
        String status,
        String history,
        LocalDateTime startDatetime,
        LocalDateTime endDatetime,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
