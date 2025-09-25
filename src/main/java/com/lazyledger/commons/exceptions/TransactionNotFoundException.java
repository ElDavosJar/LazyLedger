package com.lazyledger.commons.exceptions;

public class TransactionNotFoundException extends DomainException {
    public TransactionNotFoundException(String id) {
        super("Transaction not found with id: " + id);
    }
}