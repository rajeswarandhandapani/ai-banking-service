package com.rajeswarandhandapani.aibankingservice.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class ChatClientConfig {

        @Bean
        public ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory) {
                return builder
                                .defaultSystem(loadSystemPrompt())
                                .defaultAdvisors(
                                                SimpleLoggerAdvisor.builder().build(),
                                                MessageChatMemoryAdvisor.builder(chatMemory).build())
                                .build();
        }

        private String loadSystemPrompt() {
                ClassPathResource resource = new ClassPathResource("prompts/system-message.st");
                try (InputStream is = resource.getInputStream()) {
                        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
                } catch (IOException e) {
                        throw new IllegalStateException("Failed to load system prompt from classpath: prompts/system-message.st", e);
                }
        }

}
