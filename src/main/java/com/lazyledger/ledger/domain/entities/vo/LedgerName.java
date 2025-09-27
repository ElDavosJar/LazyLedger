package com.lazyledger.ledger.domain.entities.vo;

public record LedgerName(String value) {
    public LedgerName {
        
    }

    public static LedgerName of(String value) {
        return new LedgerName(value);
    }
    public boolean isSame(LedgerName other) {
        if (!isValid()) {
            throw new IllegalArgumentException("Invalid ledger name. It must be non-null, non-blank, and up to 100 characters.");
        }
        return this.value.equals(other.value);
    }
    private boolean isValid() {
        return value != null && !value.isBlank() && value.length() <= 100;
    }


    @Override
    public String toString() {
        return value;
    }
}