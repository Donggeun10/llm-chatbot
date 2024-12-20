package com.example.chatbot.component;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import java.util.UUID;

//https://github.com/langchain4j/langchain4j/pull/2210 //Ollama: support tools in streaming mode #2210 //1.0.0-alpha1
@AiService
public interface StreamingAssistant {

    @SystemMessage(fromResource = "/prompts/system.st")
    TokenStream streamChat(@MemoryId UUID memoryId, @UserMessage String userMessage);

}
