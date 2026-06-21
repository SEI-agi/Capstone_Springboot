package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.PatientCreateRequest;
import org.example.dto.PatientResponse;
import org.example.dto.PatientUpdateRequest;
import org.example.service.PatientService;
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
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<PatientResponse>> allPatients() {
        return ResponseEntity.ok(patientService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> patientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.findById(id));
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<PatientResponse> savePatient(@PathVariable Long userId, @Valid @RequestBody PatientCreateRequest request) {
        if (!userId.equals(request.userId())) {
            throw new IllegalArgumentException("Path userId and request userId must match.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientUpdateRequest request) {
        return ResponseEntity.ok(patientService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
//            if (patient.getStartDate() != null)
