package com.rajeswarandhandapani.aibankingservice.controller;

import jakarta.validation.constraints.NotBlank;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.stringtemplate.v4.compiler.CodeGenerator.primary_return;

import com.rajeswarandhandapani.aibankingservice.ai.llmtools.BankingServiceTools;
import com.rajeswarandhandapani.aibankingservice.ai.llmtools.DateTimeTools;
import com.rajeswarandhandapani.aibankingservice.services.BankingClient;
import com.rajeswarandhandapani.aibankingservice.utils.ApplicationSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@Validated
public class ChatClientController {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private ApplicationSecurityContext applicationSecurityContext;

    @Autowired
    private BankingServiceTools bankingServiceTools;

    @Autowired
    private BankingClient bankingClient;

    @PostMapping("/chat")
    public String chatWithAI(@NotBlank @RequestParam String userInput) {

        var tools = ToolCallbacks.from(
                new DateTimeTools(),
                bankingServiceTools
        );

        return this.chatClient.prompt()
                .toolCallbacks(tools)
                .user(userInput)
                .advisors( a -> a.param(ChatMemory.CONVERSATION_ID, applicationSecurityContext.getCurrentUser()))
                .call()
                .content();
    }

    @GetMapping("/my-accounts")
    public Object getMyAccounts() {
        return bankingClient.fetchMyAccounts().block().getBody();
    }
    

}
