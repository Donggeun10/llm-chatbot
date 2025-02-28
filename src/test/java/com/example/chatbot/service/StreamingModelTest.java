package com.example.chatbot.service;

import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@ActiveProfiles("llamaVision")
@SpringBootTest
class StreamingModelTest {

    @Autowired
    private OllamaStreamingChatModel ollamaStreamingChatModel;

    @Autowired
    private ChatMemoryProvider chatMemoryProvider;

    @Test
    void testStreamingChatModelWithImage() throws IOException, ExecutionException, InterruptedException {

        StreamingChatLanguageModel model = ollamaStreamingChatModel;

        SystemMessage systemMessage = getSystemMessage();

        Image image = Image.builder().base64Data(encodeImageToBase64("src/test/resources/bak/game_screenshot.png")).mimeType("image/png").build();
        UserMessage userMessage = UserMessage.from(
                                        TextContent.from("which color stone wins ?"),
                                        ImageContent.from(image)
                                    );

        String uuid = UUID.randomUUID().toString();
        ChatMemory chatMemory = chatMemoryProvider.get(uuid);
        chatMemory.add(userMessage);
        chatMemory.add(systemMessage);

        CompletableFuture<ChatResponse> futureResponse = new CompletableFuture<>();
        model.chat(chatMemory.messages(), new StreamingChatResponseHandler() {

            @Override
            public void onPartialResponse(String s) {
                log.debug("onPartialResponse:  {}", s);
            }

            @Override
            public void onCompleteResponse(ChatResponse chatResponse) {
                log.debug("onCompleteResponse: {}", chatResponse.aiMessage().text());
                log.debug("onCompleteResponse: {}", chatResponse);
                futureResponse.complete(chatResponse);
            }

            @Override
            public void onError(Throwable error) {
                log.error("{}:{}", error.getMessage(), error.getStackTrace()[0]);
                futureResponse.completeExceptionally(error);
            }
        });
        futureResponse.join();
        Assertions.assertNull(futureResponse.get().finishReason());
    }

    private SystemMessage getSystemMessage() {
        String systemContent = """
            You are a friendly AI assistant. Your job is to judge the outcome of the game Gomok.
             The rule of Concave is that if five stones of the same color are placed in a row, the stone of that color wins. Continuous corresponds to horizontal, vertical and diagonal lines. 
             The game board is a 19x19 grid. The intersection is the point where the horizontal and vertical lines intersect. 
             The white and black stones are placed on a yellow background with 19 lines drawn horizontally and vertically.
             The white and black stones are big round and placed on top of the intersection. 
             Please look at the image delivered and tell us the color of the winning stone. Or no winner """;

        return new SystemMessage(systemContent);
    }

    private String encodeImageToBase64(String imagePath) throws IOException {

        File file = new File(imagePath);
        log.debug("{}", file.getAbsoluteFile());
        byte[] fileContent = new byte[(int) file.length()];

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(fileContent);
        }

        return Base64.getEncoder().encodeToString(fileContent);
    }
}
