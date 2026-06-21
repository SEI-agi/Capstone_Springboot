package org.example.service;

public record AiChatContext(
        String patientName,
        String packageName,
        String packageType,
        String medicalStaffName,
        boolean hasPackage,
        boolean hasMedicalStaff,
        String sessionStatus
) {
}
