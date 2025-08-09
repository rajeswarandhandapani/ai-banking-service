package com.rajeswarandhandapani.aibankingservice.controller;

import com.rajeswarandhandapani.aibankingservice.llmtools.DateTimeTools;
import jakarta.validation.constraints.NotBlank;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.support.ToolCallbacks;
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
    public String chatWithAI(@NotBlank @RequestParam String userInput, @RequestParam String userId) {

        var tools = ToolCallbacks.from(
                new DateTimeTools()
        );

        return this.chatClient.prompt()
                .toolCallbacks(tools)
                .user(userInput)
                .advisors( a -> a.param(ChatMemory.CONVERSATION_ID, userId))
                .call()
                .content();
    }

}
