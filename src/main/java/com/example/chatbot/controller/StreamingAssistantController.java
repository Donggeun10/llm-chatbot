package com.example.chatbot.controller;


import com.example.chatbot.service.ChatService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@RestController
public class StreamingAssistantController {

    private final ChatService chatService;

    @PostMapping(value = "/chat/streaming/{user}")
    public Flux<String> chat(@PathVariable UUID user, @RequestBody String query) {

        return chatService.chatStream(user, query);
    }

}