package com.example.chatbot.component;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import java.util.UUID;

@AiService(chatMemory = "chatMemory", tools = {"AssistantTool"}, contentRetriever = "contentRetriever")
public interface Assistant {

    @SystemMessage(fromResource = "/prompts/system.st")
    String chat(@MemoryId UUID memoryId, @UserMessage String userMessage);

}
