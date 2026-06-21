package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PackageCreateRequest(
        @NotBlank @Size(max = 150) String name,
        @NotBlank String type,
        @NotNull @Positive BigDecimal price,
        @Size(max = 2000) String description,
        @NotBlank String durationType,
        LocalDateTime startDatetime,
        LocalDateTime endDatetime
) {
}
