package com.stockini.easyproducts.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.stockini.easyproducts.services.EsayProductsSpeechAgent;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;

@Configuration
public class AiConfig {
    

    @Value("${google.ai.api.key}")
    private String aiApiKey ;



    @Bean
    public ChatModel chatModel() {
        return GoogleAiGeminiChatModel.builder()
                .apiKey(aiApiKey)
                .modelName("gemini-2.5-flash")
                .temperature(0.3)
                .maxOutputTokens(1024)
                .topP(0.8)
                .build();
    }



    

    @Bean
    public EsayProductsSpeechAgent speechAgent(ChatModel chatModel) {

        return AiServices.builder(EsayProductsSpeechAgent.class)
                .chatModel(chatModel)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }
}
