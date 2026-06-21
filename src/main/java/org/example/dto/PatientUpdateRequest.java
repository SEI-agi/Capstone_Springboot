package org.example.dto;

import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record PatientUpdateRequest(
        String nextOfKinName,
        String nextOfKinPhone,
        String medicalNotes,
        String prescriptionNotes,
        @PastOrPresent LocalDate startDate,
        @PastOrPresent LocalDate endDate
) {
}
