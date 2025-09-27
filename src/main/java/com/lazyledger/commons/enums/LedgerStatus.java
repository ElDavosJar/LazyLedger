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
}
