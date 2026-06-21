package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MedicalStaffCreateRequest(
        @NotNull Long userId,
        @NotBlank @Size(max = 100) String firstName,
        @NotBlank @Size(max = 100) String lastName,
        @NotBlank @Size(max = 100) String licenseNumber,
        @Size(max = 255) String expertise,
        @Size(max = 50) String gender
) {
}
