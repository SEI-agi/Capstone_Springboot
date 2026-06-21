package org.example.service;

import org.example.dto.PatientCreateRequest;
import org.example.dto.PatientResponse;
import org.example.dto.PatientUpdateRequest;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Patient;
import org.example.model.User;
import org.example.repository.PatientRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public PatientService(PatientRepository patientRepository, UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    public List<PatientResponse> findAll() {
        return patientRepository.findAll().stream().map(DtoMapper::toPatientResponse).toList();
    }

    public PatientResponse findById(Long id) {
        return DtoMapper.toPatientResponse(findEntityById(id));
    }

    public PatientResponse create(PatientCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        Patient patient = new Patient();
        patient.setUser(user);
        patient.setNextOfKinName(request.nextOfKinName());
        patient.setNextOfKinPhone(request.nextOfKinPhone());
        patient.setMedicalNotes(request.medicalNotes());
        patient.setPrescriptionNotes(request.prescriptionNotes());
        patient.setStartDate(request.startDate());
        patient.setEndDate(request.endDate());
        return DtoMapper.toPatientResponse(patientRepository.save(patient));
    }

    public PatientResponse update(Long id, PatientUpdateRequest request) {
        Patient patient = findEntityById(id);
        if (request.nextOfKinName() != null) {
            patient.setNextOfKinName(request.nextOfKinName());
        }
        if (request.nextOfKinPhone() != null) {
            patient.setNextOfKinPhone(request.nextOfKinPhone());
        }
        if (request.medicalNotes() != null) {
            patient.setMedicalNotes(request.medicalNotes());
        }
        if (request.prescriptionNotes() != null) {
            patient.setPrescriptionNotes(request.prescriptionNotes());
        }
        if (request.startDate() != null) {
            patient.setStartDate(request.startDate());
        }
        if (request.endDate() != null) {
            patient.setEndDate(request.endDate());
        }
        return DtoMapper.toPatientResponse(patientRepository.save(patient));
    }

    public void delete(Long id) {
        patientRepository.delete(findEntityById(id));
    }

    public Patient findEntityById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found."));
    }
}
