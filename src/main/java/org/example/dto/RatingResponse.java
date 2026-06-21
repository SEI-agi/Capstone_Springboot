package org.example.dto;

import java.time.LocalDateTime;

public record RatingResponse(
        Long id,
        Long patientId,
        Long medicalStaffId,
        Long subscriptionId,
        Long chatSessionId,
        Integer ratingScore,
        String sentiment,
        String comment,
        LocalDateTime createdAt
) {
}
