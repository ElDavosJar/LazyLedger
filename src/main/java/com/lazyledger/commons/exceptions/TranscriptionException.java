package com.lazyledger.commons.exceptions;

public class TranscriptionException extends DomainException {
    public TranscriptionException(String message) {
        super(message);
    }

    public TranscriptionException(String message, Throwable cause) {
        super(message, cause);
    }
}