package org.example.service;

import org.example.model.enums.SafetyCategory;

public record AiChatReply(
        String message,
        SafetyCategory safetyCategory,
        boolean escalationRequired
) {
}
