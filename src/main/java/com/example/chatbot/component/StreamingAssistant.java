package com.example.chatbot.component;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import java.util.UUID;

//https://github.com/langchain4j/langchain4j/pull/2210
@AiService(wiringMode = EXPLICIT,  streamingChatModel = "ollamaStreamingChatModel", retrievalAugmentor = "retrievalAugmentor", chatMemoryProvider = "chatMemoryProvider")
public interface StreamingAssistant {

//    @SystemMessage(fromResource = "/prompts/system.st")
    TokenStream chat(@MemoryId UUID memoryId, @UserMessage String userMessage);

}
