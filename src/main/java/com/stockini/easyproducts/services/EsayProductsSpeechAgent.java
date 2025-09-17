package com.stockini.easyproducts.services;

import java.util.UUID;

import dev.langchain4j.data.audio.Audio;
import dev.langchain4j.data.message.AudioContent;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage ;

public interface EsayProductsSpeechAgent {

    
    @SystemMessage(
        """
            You are a speech-to-text assistant. Your role is to listen to audio input and accurately convert it into written text. You understand and can transcribe speech in English, French, and Moroccan Arabic. Always output the text clearly and preserve the language in which it was spoken. If the audio is unclear or contains multiple languages, do your best to identify and transcribe each part correctly.
        """
    )
    String chat(@MemoryId UUID memoryId ,@UserMessage Audio audio);
}
