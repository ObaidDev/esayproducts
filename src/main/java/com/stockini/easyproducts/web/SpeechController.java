package com.stockini.easyproducts.web;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
// import com.google.cloud.speech.v1.*;

import com.stockini.easyproducts.services.EsayProductsSpeechAgent;

import dev.langchain4j.data.message.AudioContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;

// import com.google.protobuf.ByteString;


@RequestMapping("/products")
@RestController
public class SpeechController {



    private final EsayProductsSpeechAgent speechAgent;

    @Autowired
    public SpeechController(EsayProductsSpeechAgent speechAgent) {
        this.speechAgent = speechAgent;
    }


    @PostMapping(value = "/transcribe" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String transcribeAudio(@RequestParam("file") MultipartFile file) throws IOException {
        
        byte[] audioBytes = file.getBytes();
        String base64Audio = Base64.getEncoder().encodeToString(audioBytes);
        
        // Determine MIME type from the file
        String mimeType = file.getContentType();
        if (mimeType == null) {
            // Fallback based on file extension or default
            String filename = file.getOriginalFilename();
            if (filename != null && filename.toLowerCase().endsWith(".mp3")) {
                mimeType = "audio/mp3";
            } else if (filename != null && filename.toLowerCase().endsWith(".wav")) {
                mimeType = "audio/wav";
            } else {
                mimeType = "audio/wav"; // default
            }
        }

        return speechAgent.chat(
            UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            UserMessage.from(
                AudioContent.from(base64Audio, mimeType),
                TextContent.from("Please transcribe this audio file. Provide only the transcribed text without any additional commentary.")
            )
        );
    }
}
    

