package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.ChatMessageResponse;
import org.example.dto.ChatRequest;
import org.example.dto.ChatResponse;
import org.example.dto.ChatSessionCreateRequest;
import org.example.dto.ChatSessionResponse;
import org.example.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/sessions/patient/{patientId}")
    public ResponseEntity<ChatSessionResponse> createSession(@PathVariable Long patientId,
                                                              @RequestBody(required = false) ChatSessionCreateRequest request) {
        // TODO: enforce that patients can only create sessions for themselves once authentication is added.
        return ResponseEntity.status(HttpStatus.CREATED).body(chatService.createSession(patientId, request));
    }

    @GetMapping("/sessions/patient/{patientId}")
    public ResponseEntity<List<ChatSessionResponse>> sessionsByPatient(@PathVariable Long patientId) {
        // TODO: enforce that patients can only access their own chat sessions once authentication is added.
        return ResponseEntity.ok(chatService.findSessionsByPatientId(patientId));
    }

    @GetMapping("/sessions/{sessionId}")
    public ResponseEntity<ChatSessionResponse> sessionById(@PathVariable Long sessionId) {
        return ResponseEntity.ok(chatService.findSessionById(sessionId));
    }

    @GetMapping("/sessions/{sessionId}/messages")
    public ResponseEntity<List<ChatMessageResponse>> messagesBySession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(chatService.findMessagesBySessionId(sessionId));
    }

    @PostMapping("/sessions/{sessionId}/messages")
    public ResponseEntity<ChatResponse> sendMessage(@PathVariable Long sessionId, @Valid @RequestBody ChatRequest request) {
        return ResponseEntity.ok(chatService.sendPatientMessage(sessionId, request));
    }

    @PutMapping("/sessions/{sessionId}/close")
    public ResponseEntity<ChatSessionResponse> closeSession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(chatService.closeSession(sessionId));
    }

    @PutMapping("/sessions/{sessionId}/escalate")
    public ResponseEntity<ChatSessionResponse> escalateSession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(chatService.escalateSession(sessionId));
    }
}