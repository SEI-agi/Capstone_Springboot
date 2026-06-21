package org.example.service;

import org.example.dto.ChatRequest;
import org.example.dto.ChatResponse;
import org.example.model.ChatMessage;
import org.example.model.ChatSession;
import org.example.model.Patient;
import org.example.model.Subscription;
import org.example.model.User;
import org.example.model.enums.ChatSessionStatus;
import org.example.model.enums.RatingSentiment;
import org.example.model.enums.SafetyCategory;
import org.example.model.enums.SenderType;
import org.example.repository.ChatMessageRepository;
import org.example.repository.ChatSessionRepository;
import org.example.repository.MedicalStaffRepository;
import org.example.repository.PatientRepository;
import org.example.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatSessionRepository chatSessionRepository;
    @Mock
    private ChatMessageRepository chatMessageRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private MedicalStaffRepository medicalStaffRepository;
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private AiChatService aiChatService;

    private ChatService chatService;

    @BeforeEach
    void setUp() {
        chatService = new ChatService(
                chatSessionRepository,
                chatMessageRepository,
                patientRepository,
                medicalStaffRepository,
                subscriptionRepository,
                aiChatService
        );
    }

    @Test
    void riskyMessageEscalatesSession() {
        User user = new User();
        user.setId(1L);
        user.setName("Ava Patient");
        user.setEmail("ava@example.com");

        Patient patient = new Patient();
        patient.setId(10L);
        patient.setUser(user);

        ChatSession session = new ChatSession();
        session.setId(100L);
        session.setPatient(patient);
        session.setStatus(ChatSessionStatus.ACTIVE);
        session.setEscalationRequired(false);

        when(chatSessionRepository.findById(100L)).thenReturn(Optional.of(session));
        when(chatSessionRepository.save(any(ChatSession.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(chatMessageRepository.save(any(ChatMessage.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(aiChatService.generateReply(any(), any())).thenReturn(new AiChatReply(
                "I’m not able to provide emergency or medical advice.",
                SafetyCategory.EMERGENCY,
                true
        ));
        when(subscriptionRepository.findTopByPatient_IdOrderByCreatedAtDesc(10L)).thenReturn(Optional.empty());

        ChatResponse response = chatService.sendPatientMessage(100L, new ChatRequest("I have chest pain"));

        assertEquals("ESCALATED", response.session().status());
        assertTrue(response.session().escalationRequired());
        assertTrue(response.responseMessage().escalationRequired());
    }
}
