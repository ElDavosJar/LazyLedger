package com.lazyledger.user.domain.entities.vo;

import java.util.regex.Pattern;

public record Email(String value) {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    public Email {
        if (value == null || !EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    public static Email of(String value) {
        return new Email(value);
    }

    public boolean isSame(Email other) {
        return this.value.equalsIgnoreCase(other.value);
    }

    @Override
    public String toString() {
        return value;
    }
}