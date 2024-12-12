package com.example.chatbot.component;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import java.util.UUID;

@AiService(streamingChatModel = "ollamaStreamingChatModel")
public interface StreamingAssistant {

    @SystemMessage(fromResource = "/prompts/system.st")
    TokenStream chat(@MemoryId UUID memoryId, @UserMessage String userMessage);

}
