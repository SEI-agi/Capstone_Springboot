package org.example.service;

import org.example.dto.ChatMessageResponse;
import org.example.dto.ChatRequest;
import org.example.dto.ChatResponse;
import org.example.dto.ChatSessionCreateRequest;
import org.example.dto.ChatSessionResponse;
import org.example.exception.ResourceNotFoundException;
import org.example.model.ChatMessage;
import org.example.model.ChatSession;
import org.example.model.MedicalStaff;
import org.example.model.Package;
import org.example.model.Patient;
import org.example.model.Subscription;
import org.example.model.enums.ChatSessionStatus;
import org.example.model.enums.SafetyCategory;
import org.example.model.enums.SenderType;
import org.example.repository.ChatMessageRepository;
import org.example.repository.ChatSessionRepository;
import org.example.repository.MedicalStaffRepository;
import org.example.repository.PatientRepository;
import org.example.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ChatService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final PatientRepository patientRepository;
    private final MedicalStaffRepository medicalStaffRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final AiChatService aiChatService;

    public ChatService(ChatSessionRepository chatSessionRepository,
                       ChatMessageRepository chatMessageRepository,
                       PatientRepository patientRepository,
                       MedicalStaffRepository medicalStaffRepository,
                       SubscriptionRepository subscriptionRepository,
                       AiChatService aiChatService) {
        this.chatSessionRepository = chatSessionRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.patientRepository = patientRepository;
        this.medicalStaffRepository = medicalStaffRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.aiChatService = aiChatService;
    }

    public ChatSessionResponse createSession(Long patientId, ChatSessionCreateRequest request) {
        Patient patient = findPatientById(patientId);
        ChatSession chatSession = new ChatSession();
        chatSession.setPatient(patient);
        chatSession.setStatus(ChatSessionStatus.ACTIVE);
        chatSession.setEscalationRequired(false);
        if (request != null && request.assignedMedicalStaffId() != null) {
            chatSession.setAssignedMedicalStaff(findMedicalStaffById(request.assignedMedicalStaffId()));
        } else {
            findLatestSubscription(patientId).map(Subscription::getMedicalStaff).ifPresent(chatSession::setAssignedMedicalStaff);
        }
        ChatSession savedSession = chatSessionRepository.save(chatSession);
        saveSystemMessage(savedSession, "Welcome to REBOUND. I can help with services, packages, app navigation, and safe general support.");
        return DtoMapper.toChatSessionResponse(savedSession);
    }

    public List<ChatSessionResponse> findSessionsByPatientId(Long patientId) {
        findPatientById(patientId);
        return chatSessionRepository.findByPatient_IdOrderByCreatedAtDesc(patientId).stream().map(DtoMapper::toChatSessionResponse).toList();
    }

    public ChatSessionResponse findSessionById(Long sessionId) {
        return DtoMapper.toChatSessionResponse(findSessionEntityById(sessionId));
    }

    public List<ChatMessageResponse> findMessagesBySessionId(Long sessionId) {
        findSessionEntityById(sessionId);
        return chatMessageRepository.findByChatSession_IdOrderByCreatedAtAsc(sessionId).stream().map(DtoMapper::toChatMessageResponse).toList();
    }

    public ChatResponse sendPatientMessage(Long sessionId, ChatRequest request) {
        ChatSession session = findSessionEntityById(sessionId);
        if (session.getStatus() == ChatSessionStatus.CLOSED) {
            throw new IllegalArgumentException("Chat session is closed.");
        }
        ChatMessage patientMessage = new ChatMessage();
        patientMessage.setChatSession(session);
        patientMessage.setSenderType(SenderType.PATIENT);
        patientMessage.setSenderUserId(session.getPatient().getUser().getId());
        patientMessage.setMessage(request.message());
        patientMessage.setEscalationRequired(false);
        patientMessage.setSafetyCategory(SafetyCategory.GENERAL);
        chatMessageRepository.save(patientMessage);

        AiChatReply reply = aiChatService.generateReply(request.message(), buildContext(session));
        ChatMessage aiMessage = new ChatMessage();
        aiMessage.setChatSession(session);
        aiMessage.setSenderType(SenderType.AI);
        aiMessage.setMessage(reply.message());
        aiMessage.setSafetyCategory(reply.safetyCategory());
        aiMessage.setEscalationRequired(reply.escalationRequired());
        chatMessageRepository.save(aiMessage);

        if (reply.escalationRequired()) {
            session.setEscalationRequired(true);
            session.setStatus(ChatSessionStatus.ESCALATED);
        }
        ChatSession savedSession = chatSessionRepository.save(session);
        return new ChatResponse(DtoMapper.toChatSessionResponse(savedSession), DtoMapper.toChatMessageResponse(aiMessage));
    }

    public ChatSessionResponse closeSession(Long sessionId) {
        ChatSession session = findSessionEntityById(sessionId);
        session.setStatus(ChatSessionStatus.CLOSED);
        session.setClosedAt(LocalDateTime.now());
        return DtoMapper.toChatSessionResponse(chatSessionRepository.save(session));
    }

    public ChatSessionResponse escalateSession(Long sessionId) {
        ChatSession session = findSessionEntityById(sessionId);
        session.setStatus(ChatSessionStatus.ESCALATED);
        session.setEscalationRequired(true);
        return DtoMapper.toChatSessionResponse(chatSessionRepository.save(session));
    }

    private ChatSession findSessionEntityById(Long sessionId) {
        return chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat session not found."));
    }

    private Patient findPatientById(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found."));
    }

    private MedicalStaff findMedicalStaffById(Long staffId) {
        return medicalStaffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Medical staff not found."));
    }

    private java.util.Optional<Subscription> findLatestSubscription(Long patientId) {
        return subscriptionRepository.findTopByPatient_IdOrderByCreatedAtDesc(patientId);
    }

    private AiChatContext buildContext(ChatSession session) {
        Subscription latestSubscription = findLatestSubscription(session.getPatient().getId()).orElse(null);
        Package carePackage = latestSubscription == null ? null : latestSubscription.getCarePackage();
        MedicalStaff medicalStaff = session.getAssignedMedicalStaff() != null ? session.getAssignedMedicalStaff() : (latestSubscription == null ? null : latestSubscription.getMedicalStaff());
        return new AiChatContext(
                session.getPatient().getUser().getName(),
                carePackage == null ? null : carePackage.getName(),
                carePackage == null || carePackage.getType() == null ? null : carePackage.getType().name(),
                medicalStaff == null ? null : medicalStaff.getFirstName() + " " + medicalStaff.getLastName(),
                carePackage != null,
                medicalStaff != null,
                session.getStatus().name()
        );
    }

    private void saveSystemMessage(ChatSession session, String message) {
        ChatMessage welcomeMessage = new ChatMessage();
        welcomeMessage.setChatSession(session);
        welcomeMessage.setSenderType(SenderType.SYSTEM);
        welcomeMessage.setMessage(message);
        welcomeMessage.setEscalationRequired(false);
        welcomeMessage.setSafetyCategory(SafetyCategory.GENERAL);
        chatMessageRepository.save(welcomeMessage);
    }
}
