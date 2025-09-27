package com.lazyledger.commons.identifiers;

import java.util.Objects;
import java.util.UUID;

public record LedgerId(UUID value) {

    private static final UUID NIL_UUID = new UUID(0L, 0L);

    public LedgerId {
        Objects.requireNonNull(value, "LedgerId value cannot be null");
        if (NIL_UUID.equals(value)) {
            throw new IllegalArgumentException("LedgerId cannot be NIL UUID (all zeros)");
        }
    }

    public static LedgerId of(UUID value) {
        return new LedgerId(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

