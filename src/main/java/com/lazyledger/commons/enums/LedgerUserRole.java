package com.lazyledger.commons.enums;

public enum LedgerUserRole {
    OWNER("OWNER"),
    ASSISTANT("ASSISTANT"),
    VIEWER("VIEWER");

    private final String value;

    LedgerUserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
