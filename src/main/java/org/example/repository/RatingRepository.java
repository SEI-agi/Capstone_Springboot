package org.example.repository;

import org.example.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByPatient_Id(Long patientId);

    List<Rating> findByMedicalStaff_Id(Long medicalStaffId);

    List<Rating> findBySubscription_Id(Long subscriptionId);

    List<Rating> findByChatSession_Id(Long chatSessionId);
}