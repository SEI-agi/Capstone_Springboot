package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.MedicalStaffCreateRequest;
import org.example.dto.MedicalStaffResponse;
import org.example.dto.MedicalStaffUpdateRequest;
import org.example.dto.PatientResponse;
import org.example.service.MedicalStaffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/medical-staff")
public class MedicalStaffController {

    private final MedicalStaffService medicalStaffService;

    public MedicalStaffController(MedicalStaffService medicalStaffService) {
        this.medicalStaffService = medicalStaffService;
    }

    @GetMapping
    public ResponseEntity<List<MedicalStaffResponse>> allMedicalStaff() {
        return ResponseEntity.ok(medicalStaffService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalStaffResponse> medicalStaffById(@PathVariable Long id) {
        return ResponseEntity.ok(medicalStaffService.findById(id));
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<MedicalStaffResponse> saveMedicalStaff(@PathVariable Long userId, @Valid @RequestBody MedicalStaffCreateRequest request) {
        if (!userId.equals(request.userId())) {
            throw new IllegalArgumentException("Path userId and request userId must match.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(medicalStaffService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalStaffResponse> updateMedicalStaff(@PathVariable Long id, @Valid @RequestBody MedicalStaffUpdateRequest request) {
        return ResponseEntity.ok(medicalStaffService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalStaff(@PathVariable Long id) {
        medicalStaffService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{staffId}/patients")
    public ResponseEntity<List<PatientResponse>> patientsUnderMedicalStaff(@PathVariable Long staffId) {
        // TODO: enforce that only authenticated staff or admins can inspect assigned patients.
        return ResponseEntity.ok(medicalStaffService.findPatientsByStaffId(staffId));
    }
}