package org.example.repository;

import org.example.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient,Long> {
    Optional<Patient> findByUser_Id(Long userId);

    List<Patient> findByUser_IdIn(List<Long> userIds);
}
