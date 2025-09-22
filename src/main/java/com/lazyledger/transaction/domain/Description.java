package com.lazyledger.transaction.domain;

//description can be null or empty, but if present should not exceed 255 characters
public record Description(String text) {
    public Description {
        if (text != null && text.length() > 255) {
            throw new IllegalArgumentException("Description must not exceed 255 characters");
        }
    }
    @Override
    public String toString() {
        return text;
    }
}
