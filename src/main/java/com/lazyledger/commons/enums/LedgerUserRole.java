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

    public static LedgerUserRole fromString(String value) {
        for (LedgerUserRole role : values()) {
            if (role.value.equals(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown LedgerUserRole: " + value);
    }
}
