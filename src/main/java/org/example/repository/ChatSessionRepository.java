package org.example.repository;

import org.example.model.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    List<ChatSession> findByPatient_IdOrderByCreatedAtDesc(Long patientId);
}