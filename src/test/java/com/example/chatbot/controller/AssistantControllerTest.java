package com.example.chatbot.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AssistantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testChatBotToolRag() throws Exception {

        mockMvc
            .perform(
                post("/chat/152f287b-5154-4c54-94cf-92f436ca4dd7") // url
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content("can you show me how to use clinic price perspective and list of  members.")
            )
            .andDo(print()) // api 수행내역 로그 출력
            .andExpect(status().isOk()); // response status 200 검증

    }

}
