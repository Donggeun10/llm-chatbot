package com.example.chatbot.controller;


import com.example.chatbot.component.Assistant;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AssistantController {

    private final Assistant assistant;

    @PostMapping(value = "/chat/{user}")
    public String chat(@PathVariable UUID user, @RequestBody String query) {

        return assistant.chat(user, query);
    }

    @GetMapping(value = "/random-uuid")
    public String chat() {

        return UUID.randomUUID().toString();
    }

    @PostMapping(value = "/chat/{user}/vision")
    public String chatVision(@PathVariable UUID user, @RequestBody String query) {

        UserMessage userMessage = UserMessage.from(
            TextContent.from(query),
            ImageContent.from("/root/img.png")
        );

        log.debug("userMessage: {}", userMessage);

        return assistant.chatVision(user, userMessage.toString());
    }

}