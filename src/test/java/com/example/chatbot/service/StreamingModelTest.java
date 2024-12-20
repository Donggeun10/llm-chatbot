package com.example.chatbot.service;

import com.example.chatbot.component.AssistantTool;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.model.output.Response;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@ActiveProfiles("llama")
@SpringBootTest
@AutoConfigureMockMvc
class StreamingModelTest {

    @Autowired
    private OllamaStreamingChatModel ollamaStreamingChatModel;

    @Autowired
    private ChatMemoryProvider chatMemoryProvider;

    @Autowired
    private AssistantTool assistantTool;

    @Test
    void testStreamingChatModel() {

        StreamingChatLanguageModel model = ollamaStreamingChatModel;

        UserMessage userMessage = new UserMessage("Tell me a joke");
        String uuid = UUID.randomUUID().toString();
        ChatMemory chatMemory = chatMemoryProvider.get(uuid);
        chatMemory.add(userMessage);

        CompletableFuture<Response<AiMessage>> futureResponse = new CompletableFuture<>();
        model.generate(chatMemory.messages(), new StreamingResponseHandler<AiMessage>() {

            @Override
            public void onNext(String token) {
                System.out.println("onNext: " + token);
            }

            @Override
            public void onComplete(Response<AiMessage> response) {
                System.out.println("onComplete: " + response);
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
        futureResponse.join();
    }
}
