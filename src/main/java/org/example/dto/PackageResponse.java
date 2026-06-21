package org.example.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PackageResponse(
        Long id,
        String name,
        String type,
        BigDecimal price,
        String description,
        String durationType,
        LocalDateTime startDatetime,
        LocalDateTime endDatetime,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
