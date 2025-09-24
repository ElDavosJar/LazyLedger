package com.lazyledger.trancriptionModule;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("doc")
public class DocTranscriber implements Transcriber {

    @Override
    public String transcribe(byte[] data) {
        // Placeholder for document to text transcription (e.g., PDF, DOCX)
        return "Transcribed text from document: [Placeholder - implement document parsing]";
    }
}