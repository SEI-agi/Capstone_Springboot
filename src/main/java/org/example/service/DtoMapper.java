package org.example.service;

import org.example.dto.ChatMessageResponse;
import org.example.dto.ChatSessionResponse;
import org.example.dto.MedicalStaffResponse;
import org.example.dto.PackageResponse;
import org.example.dto.PatientResponse;
import org.example.dto.RatingResponse;
import org.example.dto.SubscriptionResponse;
import org.example.dto.UserResponse;
import org.example.model.ChatMessage;
import org.example.model.ChatSession;
import org.example.model.MedicalStaff;
import org.example.model.Package;
import org.example.model.Patient;
import org.example.model.Rating;
import org.example.model.Subscription;
import org.example.model.User;

public final class DtoMapper {

    private DtoMapper() {
    }

    public static UserResponse toUserResponse(User user) {
        Long patientId = user.getPatient() == null ? null : user.getPatient().getId();
        Long medicalStaffId = user.getMedicalStaff() == null ? null : user.getMedicalStaff().getId();
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole() == null ? null : user.getRole().name(),
                patientId,
                medicalStaffId,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public static PatientResponse toPatientResponse(Patient patient) {
        User user = patient.getUser();
        return new PatientResponse(
                patient.getId(),
                user.getId(),
                user.getName(),
                user.getEmail(),
                patient.getNextOfKinName(),
                patient.getNextOfKinPhone(),
                patient.getMedicalNotes(),
                patient.getPrescriptionNotes(),
                patient.getStartDate(),
                patient.getEndDate(),
                patient.getCreatedAt(),
                patient.getUpdatedAt()
        );
    }

    public static MedicalStaffResponse toMedicalStaffResponse(MedicalStaff medicalStaff) {
        User user = medicalStaff.getUser();
        return new MedicalStaffResponse(
                medicalStaff.getId(),
                user.getId(),
                user.getName(),
                user.getEmail(),
                medicalStaff.getFirstName(),
                medicalStaff.getLastName(),
                medicalStaff.getLicenseNumber(),
                medicalStaff.getExpertise(),
                medicalStaff.getGender(),
                medicalStaff.getCreatedAt(),
                medicalStaff.getUpdatedAt()
        );
    }

    public static PackageResponse toPackageResponse(Package carePackage) {
        return new PackageResponse(
                carePackage.getId(),
                carePackage.getName(),
                carePackage.getType() == null ? null : carePackage.getType().name(),
                carePackage.getPrice(),
                carePackage.getDescription(),
                carePackage.getDurationType() == null ? null : carePackage.getDurationType().name(),
                carePackage.getStartDatetime(),
                carePackage.getEndDatetime(),
                carePackage.getCreatedAt(),
                carePackage.getUpdatedAt()
        );
    }

    public static SubscriptionResponse toSubscriptionResponse(Subscription subscription) {
        Patient patient = subscription.getPatient();
        Package carePackage = subscription.getCarePackage();
        MedicalStaff medicalStaff = subscription.getMedicalStaff();
        return new SubscriptionResponse(
                subscription.getId(),
                patient.getId(),
                patient.getUser().getName(),
                carePackage.getId(),
                carePackage.getName(),
                medicalStaff == null ? null : medicalStaff.getId(),
                medicalStaff == null ? null : medicalStaff.getFirstName() + " " + medicalStaff.getLastName(),
                subscription.getStatus() == null ? null : subscription.getStatus().name(),
                subscription.getHistory(),
                subscription.getStartDatetime(),
                subscription.getEndDatetime(),
                subscription.getCreatedAt(),
                subscription.getUpdatedAt()
        );
    }

    public static ChatSessionResponse toChatSessionResponse(ChatSession chatSession) {
        Patient patient = chatSession.getPatient();
        MedicalStaff medicalStaff = chatSession.getAssignedMedicalStaff();
        return new ChatSessionResponse(
                chatSession.getId(),
                patient.getId(),
                patient.getUser().getName(),
                medicalStaff == null ? null : medicalStaff.getId(),
                medicalStaff == null ? null : medicalStaff.getFirstName() + " " + medicalStaff.getLastName(),
                chatSession.getStatus() == null ? null : chatSession.getStatus().name(),
                chatSession.isEscalationRequired(),
                chatSession.getClosedAt(),
                chatSession.getCreatedAt(),
                chatSession.getUpdatedAt()
        );
    }

    public static ChatMessageResponse toChatMessageResponse(ChatMessage chatMessage) {
        return new ChatMessageResponse(
                chatMessage.getId(),
                chatMessage.getChatSession().getId(),
                chatMessage.getSenderType() == null ? null : chatMessage.getSenderType().name(),
                chatMessage.getSenderUserId(),
                chatMessage.getMessage(),
                chatMessage.getSafetyCategory() == null ? null : chatMessage.getSafetyCategory().name(),
                chatMessage.isEscalationRequired(),
                chatMessage.getCreatedAt()
        );
    }

    public static RatingResponse toRatingResponse(Rating rating) {
        return new RatingResponse(
                rating.getId(),
                rating.getPatient().getId(),
                rating.getMedicalStaff() == null ? null : rating.getMedicalStaff().getId(),
                rating.getSubscription() == null ? null : rating.getSubscription().getId(),
                rating.getChatSession() == null ? null : rating.getChatSession().getId(),
                rating.getRatingScore(),
                rating.getSentiment() == null ? null : rating.getSentiment().name(),
                rating.getComment(),
                rating.getCreatedAt()
        );
    }
}
