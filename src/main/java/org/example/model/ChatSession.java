package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.example.model.enums.ChatSessionStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_session")
public class ChatSession extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "assigned_medical_staff_id")
    private MedicalStaff assignedMedicalStaff;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatSessionStatus status = ChatSessionStatus.ACTIVE;

    @Column(nullable = false)
    private boolean escalationRequired;

    @Column
    private LocalDateTime closedAt;

    @OneToMany(mappedBy = "chatSession", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ChatMessage> messages = new ArrayList<>();

    @OneToMany(mappedBy = "chatSession", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Rating> ratings = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public MedicalStaff getAssignedMedicalStaff() {
        return assignedMedicalStaff;
    }

    public void setAssignedMedicalStaff(MedicalStaff assignedMedicalStaff) {
        this.assignedMedicalStaff = assignedMedicalStaff;
    }

    public ChatSessionStatus getStatus() {
        return status;
    }

    public void setStatus(ChatSessionStatus status) {
        this.status = status;
    }

    public boolean isEscalationRequired() {
        return escalationRequired;
    }

    public void setEscalationRequired(boolean escalationRequired) {
        this.escalationRequired = escalationRequired;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }
}