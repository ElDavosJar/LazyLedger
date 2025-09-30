package com.lazyledger.commons.exceptions;

public class LedgerNotFoundException extends DomainException {
    public LedgerNotFoundException(String id) {
        super("Ledger not found with id: " + id);
    }
}