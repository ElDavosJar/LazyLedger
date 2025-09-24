package com.lazyledger.trancriptionModule;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;
import java.util.Map;

public class AudioTranscriber {

    private static final WebClient webClient = WebClient.builder()
        .baseUrl("https://generativelanguage.googleapis.com")
        .build();

    public static String transcribeAudioToText(byte[] audioBytes, String contentType, String apiKey) {
        try {
            String base64Audio = Base64.getEncoder().encodeToString(audioBytes);

            String prompt = "Transcribe this audio file to text. Return only the transcription text, nothing else.";

            Map<String, Object> requestBody = Map.of(
                "contents", java.util.List.of(Map.of(
                    "parts", java.util.List.of(
                        Map.of("text", prompt),
                        Map.of("inline_data", Map.of(
                            "mime_type", contentType,
                            "data", base64Audio
                        ))
                    )
                )),
                "generationConfig", Map.of(
                    "temperature", 0.1,
                    "maxOutputTokens", 2048
                )
            );

            Map response = webClient.post()
                .uri("/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

            if (response != null && response.containsKey("candidates")) {
                java.util.List candidates = (java.util.List) response.get("candidates");
                if (!candidates.isEmpty()) {
                    Map candidate = (Map) candidates.get(0);
                    Map content = (Map) candidate.get("content");
                    java.util.List parts = (java.util.List) content.get("parts");
                    if (!parts.isEmpty()) {
                        Map part = (Map) parts.get(0);
                        return (String) part.get("text");
                    }
                }
            }

            return "Error transcribing audio";

        } catch (Exception e) {
            return "Error transcribing audio: " + e.getMessage();
        }
    }
}