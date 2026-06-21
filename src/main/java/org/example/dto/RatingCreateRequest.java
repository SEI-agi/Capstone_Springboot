package org.example.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RatingCreateRequest(
        @NotNull @Positive Long patientId,
        @Positive Long medicalStaffId,
        @Positive Long subscriptionId,
        @Positive Long chatSessionId,
        @NotNull @Min(1) @Max(5) Integer ratingScore,
        String sentiment,
        String comment
) {
}
