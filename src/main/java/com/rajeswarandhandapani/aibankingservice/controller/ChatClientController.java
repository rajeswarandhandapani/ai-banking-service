package com.rajeswarandhandapani.aibankingservice.controller;

import jakarta.validation.constraints.NotBlank;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class ChatClientController {

    @Autowired
    private ChatClient chatClient;

    @PostMapping("/chat")
    public String chatWithAI(@NotBlank @RequestParam String userInput) {
        return this.chatClient.prompt()
        .user(userInput)
        .call()
        .content();
    }

}
