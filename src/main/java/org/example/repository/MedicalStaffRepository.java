package org.example.repository;

import org.example.model.MedicalStaff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicalStaffRepository extends JpaRepository<MedicalStaff, Long> {
    Optional<MedicalStaff> findByUser_Id(Long userId);

    List<MedicalStaff> findByIdIn(List<Long> ids);
}