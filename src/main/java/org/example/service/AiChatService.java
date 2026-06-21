package org.example.service;

public interface AiChatService {
    AiChatReply generateReply(String userMessage, AiChatContext context);
}
