package org.example.repository;

import org.example.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByPatient_Id(Long patientId);

    java.util.Optional<Subscription> findTopByPatient_IdOrderByCreatedAtDesc(Long patientId);

    List<Subscription> findByMedicalStaff_Id(Long medicalStaffId);
}