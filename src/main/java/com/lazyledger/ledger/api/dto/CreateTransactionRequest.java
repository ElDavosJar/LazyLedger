package com.lazyledger.ledger.api.dto;

import java.math.BigDecimal;

public record CreateTransactionRequest(
    String ledgerId,
    BigDecimal amount,
    String currency,
    String description,
    String category,
    String transactionDate
) {
    public static CreateTransactionRequest from(String ledgerId, BigDecimal amount, String currency,
                                              String description, String category, String transactionDate) {
        return new CreateTransactionRequest(ledgerId, amount, currency, description, category, transactionDate);
    }
}