package com.example.chatbot.controller;


import com.example.chatbot.component.Assistant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}