package org.example.service;

import org.example.model.enums.SafetyCategory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RuleBasedAiChatServiceTest {

    private final RuleBasedAiChatService service = new RuleBasedAiChatService();

    @Test
    void riskyMessageTriggersEscalation() {
        AiChatReply reply = service.generateReply(
                "I have chest pain and shortness of breath",
                new AiChatContext("Ava", "Home Care", "FULL_DAY", "Nina Brown", true, true, "ACTIVE")
        );

        assertTrue(reply.escalationRequired());
        assertTrue(reply.message().toLowerCase().contains("emergency"));
        assertTrue(reply.safetyCategory() == SafetyCategory.EMERGENCY);
    }

    @Test
    void generalPackageQuestionReturnsSafeHelp() {
        AiChatReply reply = service.generateReply(
                "Can you explain my package and 24/7 support?",
                new AiChatContext("Ava", "Home Care", "FULL_DAY", "Nina Brown", true, true, "ACTIVE")
        );

        assertFalse(reply.escalationRequired());
        assertTrue(reply.message().contains("REBOUND"));
    }
}
