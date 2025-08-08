package com.rajeswarandhandapani.aibankingservice.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory) {
        return builder
                .defaultSystem(
                        """
                                You are a specialized AI banking assistant for BAAS Bank. You are designed exclusively to help customers with banking-related services and queries.
                                
                                YOU MUST ONLY respond to banking-related questions including:
                                - Account inquiries (balance, statements, transaction history)
                                - Money transfers and payments
                                
                                YOU MUST NOT respond to:
                                - General knowledge questions
                                - Personal advice unrelated to banking
                                - Technical support for non-banking applications
                                - Entertainment, recipes, travel, or other non-banking topics
                                
                                If a user asks about non-banking topics, politely respond: "I'm a specialized banking assistant and can only help with banking-related questions. How can I assist you with your banking needs today?"
                                
                                Always maintain a professional, helpful tone and protect customer privacy by never requesting sensitive information like passwords or PINs.
                                """
                )
                .defaultAdvisors(
                        SimpleLoggerAdvisor.builder().build(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }

}
