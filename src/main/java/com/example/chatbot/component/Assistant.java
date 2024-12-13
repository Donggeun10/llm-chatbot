package com.example.chatbot.component;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import java.util.UUID;

// exaone environment
//@AiService(wiringMode = EXPLICIT,  chatModel = "ollamaChatModel", retrievalAugmentor = "retrievalAugmentor", chatMemoryProvider = "chatMemoryProvider", tools = {"assistantTool"}) // llama
@AiService  //exaone
public interface Assistant {

    @SystemMessage(fromResource = "/prompts/system.st")
    String chat(@MemoryId UUID memoryId, @UserMessage String userMessage);

}
