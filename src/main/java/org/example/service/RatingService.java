package org.example.service;

import org.example.dto.RatingCreateRequest;
import org.example.dto.RatingResponse;
import org.example.exception.ResourceNotFoundException;
import org.example.model.ChatSession;
import org.example.model.MedicalStaff;
import org.example.model.Patient;
import org.example.model.Rating;
import org.example.model.Subscription;
import org.example.model.enums.RatingSentiment;
import org.example.repository.ChatSessionRepository;
import org.example.repository.MedicalStaffRepository;
import org.example.repository.PatientRepository;
import org.example.repository.RatingRepository;
import org.example.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RatingService {

    private final RatingRepository ratingRepository;
    private final PatientRepository patientRepository;
    private final MedicalStaffRepository medicalStaffRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ChatSessionRepository chatSessionRepository;

    public RatingService(RatingRepository ratingRepository,
                         PatientRepository patientRepository,
                         MedicalStaffRepository medicalStaffRepository,
                         SubscriptionRepository subscriptionRepository,
                         ChatSessionRepository chatSessionRepository) {
        this.ratingRepository = ratingRepository;
        this.patientRepository = patientRepository;
        this.medicalStaffRepository = medicalStaffRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.chatSessionRepository = chatSessionRepository;
    }

    public List<RatingResponse> findAll() {
        return ratingRepository.findAll().stream().map(DtoMapper::toRatingResponse).toList();
    }

    public RatingResponse findById(Long id) {
        return DtoMapper.toRatingResponse(findEntityById(id));
    }

    public List<RatingResponse> findByPatientId(Long patientId) {
        patientRepository.findById(patientId).orElseThrow(() -> new ResourceNotFoundException("Patient not found."));
        return ratingRepository.findByPatient_Id(patientId).stream().map(DtoMapper::toRatingResponse).toList();
    }

    public List<RatingResponse> findByMedicalStaffId(Long staffId) {
        medicalStaffRepository.findById(staffId).orElseThrow(() -> new ResourceNotFoundException("Medical staff not found."));
        return ratingRepository.findByMedicalStaff_Id(staffId).stream().map(DtoMapper::toRatingResponse).toList();
    }

    public RatingResponse create(RatingCreateRequest request) {
        Rating rating = new Rating();
        rating.setPatient(findPatientById(request.patientId()));
        if (request.medicalStaffId() != null) {
            rating.setMedicalStaff(findMedicalStaffById(request.medicalStaffId()));
        }
        if (request.subscriptionId() != null) {
            rating.setSubscription(findSubscriptionById(request.subscriptionId()));
        }
        if (request.chatSessionId() != null) {
            rating.setChatSession(findChatSessionById(request.chatSessionId()));
        }
        rating.setRatingScore(request.ratingScore());
        rating.setSentiment(parseSentiment(request.sentiment()));
        rating.setComment(request.comment());
        return DtoMapper.toRatingResponse(ratingRepository.save(rating));
    }

    public void delete(Long id) {
        ratingRepository.delete(findEntityById(id));
    }

    public Rating findEntityById(Long id) {
        return ratingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found."));
    }

    private Patient findPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found."));
    }

    private MedicalStaff findMedicalStaffById(Long id) {
        return medicalStaffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical staff not found."));
    }

    private Subscription findSubscriptionById(Long id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found."));
    }

    private ChatSession findChatSessionById(Long id) {
        return chatSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chat session not found."));
    }

    private RatingSentiment parseSentiment(String value) {
        if (value == null || value.isBlank()) {
            return RatingSentiment.NEUTRAL;
        }
        return RatingSentiment.valueOf(value.trim().toUpperCase());
    }
}
