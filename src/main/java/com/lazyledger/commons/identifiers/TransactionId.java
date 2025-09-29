package com.lazyledger.commons.identifiers;

import java.util.Objects;
import java.util.UUID;

public record TransactionId(UUID value) {

    private static final UUID NIL_UUID = new UUID(0L, 0L);

    public TransactionId {
        Objects.requireNonNull(value, "TransactionId value cannot be null");
        if (NIL_UUID.equals(value)) {
            throw new IllegalArgumentException("TransactionId cannot be NIL UUID (all zeros)");
        }
    }

    public static TransactionId of(UUID value) {
        return new TransactionId(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
