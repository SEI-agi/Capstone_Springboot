package org.example.dto;

import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record SubscriptionUpdateRequest(
        @Positive Long medicalStaffId,
        String status,
        String history,
        LocalDateTime startDatetime,
        LocalDateTime endDatetime
) {
}
