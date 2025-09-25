package com.lazyledger.trancriptionModule;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import java.nio.file.Path;

import java.nio.file.Files;
import com.lazyledger.transcription.transcribers.AudioTranscriber;

class AudioTranscriberTest {

    @Test
    void testTranscribeAudioToText() throws Exception {
        // Load the audio file from resources
        ClassPathResource resource = new ClassPathResource("audio/WhatsApp Ptt 2025-09-23 at 18.46.29 (1).ogg");
        Path filePath = resource.getFile().toPath();
        byte[] audioBytes = Files.readAllBytes(filePath);

        // Transcribe the audio to text
        String contentType = "audio/ogg";
        String apiKey = "AIzaSyC4grZUp-8en0MjFYXXiFZivmHHgL-9aMo"; // From application.properties
        String transcription = AudioTranscriber.transcribeAudioToText(audioBytes, contentType, apiKey);

        // Print the result
        System.out.println("Transcription result: " + transcription);
    }
}