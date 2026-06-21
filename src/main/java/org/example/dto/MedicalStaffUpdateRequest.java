package org.example.dto;

import jakarta.validation.constraints.Size;

public record MedicalStaffUpdateRequest(
        @Size(max = 100) String firstName,
        @Size(max = 100) String lastName,
        @Size(max = 100) String licenseNumber,
        @Size(max = 255) String expertise,
        @Size(max = 50) String gender
) {
}
