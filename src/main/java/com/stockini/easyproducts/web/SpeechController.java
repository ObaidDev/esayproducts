package com.stockini.easyproducts.web;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.google.cloud.speech.v1.*;

import com.google.protobuf.ByteString;


@RequestMapping("/products")
@RestController
public class SpeechController {



    @PostMapping(value = "/transcribe" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String transcribeAudio(@RequestParam("file") MultipartFile file) throws IOException {
        try (SpeechClient speechClient = SpeechClient.create()) {

            // Convert audio file to bytes
            ByteString audioBytes = ByteString.copyFrom(file.getBytes());

            // Configure request
            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16) // or FLAC/MP3 depending on your file
                    .setLanguageCode("en-US")
                    .build();

            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setContent(audioBytes)
                    .build();

            // Transcribe
            RecognizeResponse response = speechClient.recognize(config, audio);
            StringBuilder transcript = new StringBuilder();
            for (SpeechRecognitionResult result : response.getResultsList()) {
                transcript.append(result.getAlternatives(0).getTranscript());
            }

            return transcript.toString();
        }
    }
    
}
