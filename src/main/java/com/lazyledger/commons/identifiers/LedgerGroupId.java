package com.lazyledger.commons.identifiers;
import java.util.Objects;
import java.util.UUID;

public record LedgerGroupId(UUID value) {

    private static final UUID NIL_UUID = new UUID(0L, 0L);

    public LedgerGroupId {
        Objects.requireNonNull(value, "LedgerGroupId value cannot be null");
        if (NIL_UUID.equals(value)) {
            throw new IllegalArgumentException("LedgerGroupId cannot be NIL UUID (all zeros)");
        }
    }

    public static LedgerGroupId of(UUID value) {
        return new LedgerGroupId(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

