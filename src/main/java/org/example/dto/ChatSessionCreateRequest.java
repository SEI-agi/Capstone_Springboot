package org.example.dto;

import jakarta.validation.constraints.Positive;

public record ChatSessionCreateRequest(
        @Positive Long assignedMedicalStaffId,
        String initialTopic
) {
}
