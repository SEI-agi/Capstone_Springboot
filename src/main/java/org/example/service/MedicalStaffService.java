package org.example.service;

import org.example.dto.MedicalStaffCreateRequest;
import org.example.dto.MedicalStaffResponse;
import org.example.dto.MedicalStaffUpdateRequest;
import org.example.dto.PatientResponse;
import org.example.exception.ResourceNotFoundException;
import org.example.model.MedicalStaff;
import org.example.model.Patient;
import org.example.model.User;
import org.example.repository.MedicalStaffRepository;
import org.example.repository.PatientRepository;
import org.example.repository.SubscriptionRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class MedicalStaffService {

    private final MedicalStaffRepository medicalStaffRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final SubscriptionRepository subscriptionRepository;

    public MedicalStaffService(MedicalStaffRepository medicalStaffRepository,
                               UserRepository userRepository,
                               PatientRepository patientRepository,
                               SubscriptionRepository subscriptionRepository) {
        this.medicalStaffRepository = medicalStaffRepository;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<MedicalStaffResponse> findAll() {
        return medicalStaffRepository.findAll().stream().map(DtoMapper::toMedicalStaffResponse).toList();
    }

    public MedicalStaffResponse findById(Long id) {
        return DtoMapper.toMedicalStaffResponse(findEntityById(id));
    }

    public MedicalStaffResponse create(MedicalStaffCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        MedicalStaff medicalStaff = new MedicalStaff();
        medicalStaff.setUser(user);
        medicalStaff.setFirstName(request.firstName());
        medicalStaff.setLastName(request.lastName());
        medicalStaff.setLicenseNumber(request.licenseNumber());
        medicalStaff.setExpertise(request.expertise());
        medicalStaff.setGender(request.gender());
        return DtoMapper.toMedicalStaffResponse(medicalStaffRepository.save(medicalStaff));
    }

    public MedicalStaffResponse update(Long id, MedicalStaffUpdateRequest request) {
        MedicalStaff medicalStaff = findEntityById(id);
        if (request.firstName() != null) {
            medicalStaff.setFirstName(request.firstName());
        }
        if (request.lastName() != null) {
            medicalStaff.setLastName(request.lastName());
        }
        if (request.licenseNumber() != null) {
            medicalStaff.setLicenseNumber(request.licenseNumber());
        }
        if (request.expertise() != null) {
            medicalStaff.setExpertise(request.expertise());
        }
        if (request.gender() != null) {
            medicalStaff.setGender(request.gender());
        }
        return DtoMapper.toMedicalStaffResponse(medicalStaffRepository.save(medicalStaff));
    }

    public void delete(Long id) {
        medicalStaffRepository.delete(findEntityById(id));
    }

    public List<PatientResponse> findPatientsByStaffId(Long staffId) {
        findEntityById(staffId);
        Map<Long, Patient> patients = new LinkedHashMap<>();
        subscriptionRepository.findByMedicalStaff_Id(staffId).forEach(subscription -> patients.put(subscription.getPatient().getId(), subscription.getPatient()));
        return patients.values().stream().map(DtoMapper::toPatientResponse).toList();
    }

    public MedicalStaff findEntityById(Long id) {
        return medicalStaffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical staff not found."));
    }
}
