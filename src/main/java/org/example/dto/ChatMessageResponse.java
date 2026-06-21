package org.example.dto;

import java.time.LocalDateTime;

public record ChatMessageResponse(
        Long id,
        Long chatSessionId,
        String senderType,
        Long senderUserId,
        String message,
        String safetyCategory,
        boolean escalationRequired,
        LocalDateTime createdAt
) {
}
