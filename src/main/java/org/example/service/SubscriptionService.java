package org.example.service;

import org.example.dto.SubscriptionCreateRequest;
import org.example.dto.SubscriptionResponse;
import org.example.dto.SubscriptionUpdateRequest;
import org.example.exception.ResourceNotFoundException;
import org.example.model.MedicalStaff;
import org.example.model.Patient;
import org.example.model.Package;
import org.example.model.Subscription;
import org.example.model.enums.SubscriptionStatus;
import org.example.repository.MedicalStaffRepository;
import org.example.repository.PackageRepository;
import org.example.repository.PatientRepository;
import org.example.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class SubscriptionService {

	private final SubscriptionRepository subscriptionRepository;
	private final PatientRepository patientRepository;
	private final PackageRepository packageRepository;
	private final MedicalStaffRepository medicalStaffRepository;

	public SubscriptionService(SubscriptionRepository subscriptionRepository,
							   PatientRepository patientRepository,
							   PackageRepository packageRepository,
							   MedicalStaffRepository medicalStaffRepository) {
		this.subscriptionRepository = subscriptionRepository;
		this.patientRepository = patientRepository;
		this.packageRepository = packageRepository;
		this.medicalStaffRepository = medicalStaffRepository;
	}

	public List<SubscriptionResponse> findAll() {
		return subscriptionRepository.findAll().stream().map(DtoMapper::toSubscriptionResponse).toList();
	}

	public SubscriptionResponse findById(Long id) {
		return DtoMapper.toSubscriptionResponse(findEntityById(id));
	}

	public List<SubscriptionResponse> findByPatientId(Long patientId) {
		findPatientById(patientId);
		return subscriptionRepository.findByPatient_Id(patientId).stream().map(DtoMapper::toSubscriptionResponse).toList();
	}

	public SubscriptionResponse create(Long patientId, Long packageId, SubscriptionCreateRequest request) {
		Patient patient = findPatientById(patientId);
		Package carePackage = findPackageById(packageId);
		Subscription subscription = new Subscription();
		subscription.setPatient(patient);
		subscription.setCarePackage(carePackage);
		if (request.medicalStaffId() != null) {
			subscription.setMedicalStaff(findMedicalStaffById(request.medicalStaffId()));
		}
		subscription.setStatus(request.status() == null ? SubscriptionStatus.ACTIVE : parseStatus(request.status()));
		subscription.setHistory(request.history());
		subscription.setStartDatetime(request.startDatetime());
		subscription.setEndDatetime(request.endDatetime());
		return DtoMapper.toSubscriptionResponse(subscriptionRepository.save(subscription));
	}

	public SubscriptionResponse assignMedicalStaff(Long subscriptionId, Long staffId) {
		Subscription subscription = findEntityById(subscriptionId);
		subscription.setMedicalStaff(findMedicalStaffById(staffId));
		return DtoMapper.toSubscriptionResponse(subscriptionRepository.save(subscription));
	}

	public SubscriptionResponse update(Long subscriptionId, SubscriptionUpdateRequest request) {
		Subscription subscription = findEntityById(subscriptionId);
		if (request.medicalStaffId() != null) {
			subscription.setMedicalStaff(findMedicalStaffById(request.medicalStaffId()));
		}
		if (request.status() != null) {
			subscription.setStatus(parseStatus(request.status()));
		}
		if (request.history() != null) {
			subscription.setHistory(request.history());
		}
		if (request.startDatetime() != null) {
			subscription.setStartDatetime(request.startDatetime());
		}
		if (request.endDatetime() != null) {
			subscription.setEndDatetime(request.endDatetime());
		}
		return DtoMapper.toSubscriptionResponse(subscriptionRepository.save(subscription));
	}

	public SubscriptionResponse cancel(Long subscriptionId) {
		Subscription subscription = findEntityById(subscriptionId);
		subscription.setStatus(SubscriptionStatus.CANCELLED);
		if (subscription.getHistory() == null || subscription.getHistory().isBlank()) {
			subscription.setHistory("Subscription cancelled.");
		} else {
			subscription.setHistory(subscription.getHistory() + " | Cancelled on " + LocalDateTime.now());
		}
		return DtoMapper.toSubscriptionResponse(subscriptionRepository.save(subscription));
	}

	public void delete(Long id) {
		subscriptionRepository.delete(findEntityById(id));
	}

	public Subscription findEntityById(Long id) {
		return subscriptionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Subscription not found."));
	}

	private Patient findPatientById(Long id) {
		return patientRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Patient not found."));
	}

	private Package findPackageById(Long id) {
		return packageRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Package not found."));
	}

	private MedicalStaff findMedicalStaffById(Long id) {
		return medicalStaffRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Medical staff not found."));
	}

	private SubscriptionStatus parseStatus(String value) {
		return SubscriptionStatus.valueOf(value.trim().toUpperCase());
	}
}
//package org.example.service;
//
//import org.example.model.Subscription;
//import org.example.repository.SubscriptionRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class SubscriptionService {
//    @Autowired
//    private SubscriptionRepository subscriptionRepository;
//
//    public Subscription saveSubscription(Subscription subscription){
//        return subscriptionRepository.save(subscription);
//
//    }
//    public List<Subscription> allFeedback(){
//        return subscriptionRepository.findAll();
//    }
//}
