package com.stockini.easyproducts.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
// import com.google.cloud.speech.v1.*;
import static dev.langchain4j.internal.Utils.readBytes;

import com.stockini.easyproducts.dtos.ProductResponse;
import com.stockini.easyproducts.services.EsayProductsSpeechAgent;
import com.stockini.easyproducts.services.ProductExtractionAgent;

import dev.langchain4j.data.audio.Audio;
import dev.langchain4j.data.message.AudioContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.audio.AudioTranscriptionRequest;
import dev.langchain4j.model.audio.AudioTranscriptionResponse;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/products")
public class SpeechController {



    private final EsayProductsSpeechAgent speechAgent;
    private final ProductExtractionAgent productAgent;

    private final GoogleAiGeminiChatModel gemini;

    @Autowired
    public SpeechController(EsayProductsSpeechAgent speechAgent, ProductExtractionAgent productAgent, @Qualifier("geminiChatModel") GoogleAiGeminiChatModel gemini) {
        this.speechAgent = speechAgent;
        this.productAgent = productAgent;
        this.gemini = gemini;
    }


    @PostMapping(value = "/transcribe" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String transcribeAudio(@RequestParam("file") MultipartFile file) throws IOException {

        return "not implemented yet";
        
        // byte[] audioBytes = file.getBytes();
        // String base64Audio = Base64.getEncoder().encodeToString(audioBytes);
        
        // // Determine MIME type from the file
        // String mimeType = file.getContentType();
        // if (mimeType == null) {
        //     // Fallback based on file extension or default
        //     String filename = file.getOriginalFilename();
        //     if (filename != null && filename.toLowerCase().endsWith(".mp3")) {
        //         mimeType = "audio/mp3";
        //     } else if (filename != null && filename.toLowerCase().endsWith(".wav")) {
        //         mimeType = "audio/wav";
        //     } else {
        //         mimeType = "audio/wav"; // default
        //     }
        // }

        // return speechAgent.chat(
        //     UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
        //     UserMessage.from(
        //         AudioContent.from(base64Audio, mimeType),
        //         TextContent.from("Please transcribe this audio file. Provide only the transcribed text without any additional commentary.")
        //     )
        // );
    }



    @PostMapping("/test")
    public String test() throws IOException {

        // TODO use local file
        byte[] bytes = readBytes("file:///home/plutus/java/audio/speech-94649.mp3");
        String base64Data = new String(Base64.getEncoder().encode(bytes));

        UserMessage userMessage = UserMessage.from(
                AudioContent.from(base64Data, "audio/mp3"),
                TextContent.from("Transcribe the audio.")
        );

        // when
        ChatResponse response = gemini.chat(userMessage);
        return response.toString();

    }


    @PostMapping("/test2")
    public ProductResponse test2() {

        String response = """
                We currently have 4 Nike Air Max shoes, 2 MacBook Pro laptops, and 6 Adidas running t-shirts in stock. 
                Also, there are 3 Sony noise cancelling headphones and 5 Apple iPhone 15 Pro Max devices.
                """;

        return productAgent.extractAndEnrich(response);

    }
}
    

