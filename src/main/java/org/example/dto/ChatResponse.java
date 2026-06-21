package org.example.dto;

public record ChatResponse(
        ChatSessionResponse session,
        ChatMessageResponse responseMessage
) {
}
