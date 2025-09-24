package com.lazyledger.trancriptionModule;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("image")
public class ImageTranscriber implements Transcriber {

    @Override
    public String transcribe(byte[] data) {

        return "Transcribed text from image: [Placeholder - implement OCR]";
    }
}