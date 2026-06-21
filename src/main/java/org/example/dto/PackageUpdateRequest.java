package org.example.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PackageUpdateRequest(
        @Size(max = 150) String name,
        String type,
        @Positive BigDecimal price,
        @Size(max = 2000) String description,
        String durationType,
        LocalDateTime startDatetime,
        LocalDateTime endDatetime
) {
}
