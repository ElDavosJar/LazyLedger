package com.lazyledger.ledger.domain.entities.transaction.domain;

import java.time.LocalDate;
import java.util.Objects;


public record TransactionDate(LocalDate value) {

    public TransactionDate {
        Objects.requireNonNull(value, "Transaction date value cannot be null after initialization");

        if (value.isBefore(LocalDate.of(2000, 1, 1))) {
            throw new IllegalArgumentException("Transaction date cannot be before the year 2000");
        }
        if (value.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Transaction date cannot be in the future");
        }
    }

    public static TransactionDate of(LocalDate date) {
        LocalDate finalDate = (date == null) ? LocalDate.now() : date;
        return new TransactionDate(finalDate);
    }

    public static TransactionDate fromString(String dateString) {
        if (dateString == null || dateString.isBlank()) {
            return TransactionDate.of(null);
        }
        try {
            return TransactionDate.of(LocalDate.parse(dateString));
        } catch (java.time.format.DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use YYYY-MM-DD.", e);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}