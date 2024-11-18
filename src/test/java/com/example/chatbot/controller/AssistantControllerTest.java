package com.example.chatbot.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@Slf4j
@ActiveProfiles("local")
@SpringBootTest
@AutoConfigureMockMvc
class AssistantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String uuid = "";

    @BeforeEach
    void setup() {
        uuid = UUID.randomUUID().toString();
        log.debug("uuid: {}", uuid);
    }

    @Test
    void testChatBotToolRag() throws Exception {

        mockMvc
            .perform(
                post("/chat/"+uuid) // url
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content("can you show me clinic price and list of members.")
            )
            .andDo(print()) // api 수행내역 로그 출력
            .andExpect(status().isOk()); // response status 200 검증

    }

    @Test
    void testRememberMe() throws Exception {

        mockMvc
            .perform(
                post("/chat/"+uuid) // url
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content("Hi, I am robot. Nice to see you")
            )
            .andDo(print()) // api 수행내역 로그 출력
            .andExpect(status().isOk()); // response status 200 검증

        mockMvc
            .perform(
                post("/chat/"+uuid) // url
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content("Hi, Do you remember my name?")
            )
            .andDo(print()) // api 수행내역 로그 출력
            .andExpect(status().isOk()); // response status 200 검증

    }

    @Test
    void testUuid() throws Exception {

        mockMvc
            .perform(
                get("/random-uuid") // url
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
            )
            .andDo(print()) // api 수행내역 로그 출력
            .andExpect(status().isOk()); // response status 200 검증

    }
}
