package com.lazyledger.trancriptionModule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TranscriptionService {

    @Autowired
    private TranscriberFactory transcriberFactory;

    @Value("${google.ai.api.key}")
    private String apiKey;

    public List<TransactionDataDto> transcribeAndExtractTransactions(byte[] binaryData, String mediaType) {
        String text;
        if ("text".equals(mediaType)) {
            text = new String(binaryData);
        } else {
            Transcriber transcriber = transcriberFactory.getTranscriber(mediaType);
            text = transcriber.transcribe(binaryData);
        }
        return DataExtractor.extractTransactionData(text, apiKey);
    }
}