package org.example.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record SubscriptionCreateRequest(
        @NotNull @Positive Long patientId,
        @NotNull @Positive Long packageId,
        @Positive Long medicalStaffId,
        String status,
        String history,
        @NotNull LocalDateTime startDatetime,
        LocalDateTime endDatetime
) {
}
