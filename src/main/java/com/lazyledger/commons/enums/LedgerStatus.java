package com.lazyledger.commons.enums;

public enum LedgerStatus {
    ACTIVE("active"),
    INACTIVE("inactive"),
    ARCHIVED("archived");

    private final String value;

    LedgerStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static LedgerStatus fromString(String value) {
        for (LedgerStatus status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown LedgerStatus: " + value);
    }
}
