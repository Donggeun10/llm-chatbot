package com.example.chatbot;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Theme("hilla")
@SpringBootApplication
public class ChatbotApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(ChatbotApplication.class, args);
    }

}
