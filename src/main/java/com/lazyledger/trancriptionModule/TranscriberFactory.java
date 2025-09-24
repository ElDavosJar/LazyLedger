package com.lazyledger.trancriptionModule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class TranscriberFactory {

    @Autowired
    private AudioTranscriber audioTranscriber;

    @Autowired
    private ImageTranscriber imageTranscriber;

    @Autowired
    private DocTranscriber docTranscriber;

    private final Map<String, Transcriber> transcriberMap = new HashMap<>();

    @PostConstruct
    public void init() {
        transcriberMap.put("audio", audioTranscriber);
        transcriberMap.put("image", imageTranscriber);
        transcriberMap.put("doc", docTranscriber);
    }

    public Transcriber getTranscriber(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Transcriber type cannot be null");
        }
        Transcriber transcriber = transcriberMap.get(type.toLowerCase());
        if (transcriber == null) {
            throw new IllegalArgumentException("Unknown transcriber type: " + type);
        }
        return transcriber;
    }
}