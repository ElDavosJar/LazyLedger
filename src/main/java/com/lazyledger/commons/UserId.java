package com.lazyledger.commons;

import java.util.Objects;
import java.util.UUID;

public record UserId(UUID value) {

    private static final UUID NIL_UUID = new UUID(0L, 0L);

    public UserId {
        Objects.requireNonNull(value, "UserId value cannot be null");
        if (NIL_UUID.equals(value)) {
            throw new IllegalArgumentException("UserId cannot be NIL UUID (all zeros)");
        }
    }

    public static UserId of(UUID value) {
        return new UserId(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
