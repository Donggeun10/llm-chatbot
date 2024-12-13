package com.example.chatbot.service;

import com.example.chatbot.component.StreamingAssistant;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@BrowserCallable
@AnonymousAllowed
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final StreamingAssistant streamingAssistant;

    public Flux<String> chatStream(UUID user, String message) {
        Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();

        streamingAssistant.chat(user, message)
            .onRetrieved(source -> log.info("Retrieved source: {}", source))
            .onNext(sink::tryEmitNext)
            .onComplete(c -> sink.tryEmitComplete())
            .onError(sink::tryEmitError)
            .start();

        return sink.asFlux();
    }

}
