package org.example.service;

import org.example.model.enums.SafetyCategory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class RuleBasedAiChatService implements AiChatService {

    private static final List<String> EMERGENCY_KEYWORDS = List.of(
            "chest pain",
            "difficulty breathing",
            "shortness of breath",
            "severe bleeding",
            "stroke",
            "fainting",
            "overdose",
            "suicidal",
            "suicide",
            "allergic reaction",
            "severe pain",
            "medication dosage",
            "prescription change",
            "cannot breathe",
            "heart attack",
            "seizure",
            "unconscious",
            "high fever",
            "fall injury",
            "severe headache",
            "confusion",
            "emergency"
    );

    @Value("${ai.provider:rule-based}")
    private String provider;

    @Override
    public AiChatReply generateReply(String userMessage, AiChatContext context) {
        String normalizedMessage = userMessage.toLowerCase(Locale.ROOT);

        if (containsAny(normalizedMessage, EMERGENCY_KEYWORDS)) {
            return new AiChatReply(
                    "I’m not able to provide emergency or medical advice. Please contact emergency services immediately or reach your assigned nurse or care team. I’ve marked this conversation for human follow-up.",
                    SafetyCategory.EMERGENCY,
                    true
            );
        }

        if (containsAny(normalizedMessage, List.of("diagnose", "diagnosis", "prescribe", "dosage", "dose", "medication", "prescription"))) {
            return new AiChatReply(
                    "I can’t diagnose, prescribe, or change medication guidance. I can help with REBOUND services, app navigation, or connect you to human support.",
                    SafetyCategory.CLINICAL,
                    true
            );
        }

        if (containsAny(normalizedMessage, List.of("package", "plan", "subscription", "service", "half-day", "full-day", "home care", "care customization", "24/7", "live chat", "support"))) {
            return new AiChatReply(buildServiceResponse(context), SafetyCategory.PACKAGE_INFO, false);
        }

        if (containsAny(normalizedMessage, List.of("where", "how do i", "how to", "navigation", "app", "menu", "profile", "booking", "chat", "history"))) {
            return new AiChatReply(
                    "I can help you navigate REBOUND. You can review your care plan, view your chat history, and see your assigned support details in the app. If you want, tell me what screen you are looking for and I’ll guide you.",
                    SafetyCategory.NAVIGATION,
                    false
            );
        }

        if (containsAny(normalizedMessage, List.of("hi", "hello", "hey", "good morning", "good afternoon", "good evening"))) {
            return new AiChatReply(
                    "Hello. I’m the REBOUND support assistant. I can explain services, help you navigate the app, summarize your assigned package or support contact, and route urgent or clinical questions to a human team member.",
                    SafetyCategory.GENERAL,
                    false
            );
        }

        return new AiChatReply(
                buildGeneralResponse(context),
                SafetyCategory.GENERAL,
                false
        );
    }

    private String buildGeneralResponse(AiChatContext context) {
        StringBuilder response = new StringBuilder();
        response.append("I’m here to help with REBOUND services, app navigation, and general support. ");
        response.append("I can’t provide diagnosis, treatment, prescriptions, or emergency advice. ");
        if (context.hasPackage()) {
            response.append("Your assigned package is ").append(context.packageName());
            if (context.packageType() != null) {
                response.append(" (" + context.packageType() + ")");
            }
            response.append(". ");
        }
        if (context.hasMedicalStaff()) {
            response.append("Your assigned care contact is ").append(context.medicalStaffName()).append(". ");
        }
        response.append("Tell me what you need and I’ll point you to the right place.");
        return response.toString();
    }

    private String buildServiceResponse(AiChatContext context) {
        StringBuilder response = new StringBuilder();
        response.append("REBOUND offers personalized home care, patient support, care packages, nurse and medical staff assistance, and 24/7 support. ");
        response.append("We can help with half-day hire, full-day hire, home care, customized care plans, patient care networking, and live chat support. ");
        if (context.hasPackage()) {
            response.append("Your current package is ").append(context.packageName());
            if (context.packageType() != null) {
                response.append(" (" + context.packageType() + ")");
            }
            response.append(". ");
        }
        if (context.hasMedicalStaff()) {
            response.append("Your assigned medical staff contact is ").append(context.medicalStaffName()).append(". ");
        }
        response.append("If you need medical advice or anything urgent, I will escalate you to human support.");
        return response.toString();
    }

    private boolean containsAny(String message, List<String> keywords) {
        for (String keyword : keywords) {
            if (message.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}
