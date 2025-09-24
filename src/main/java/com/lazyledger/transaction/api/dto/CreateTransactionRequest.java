package com.lazyledger.transaction.api.dto;

import java.math.BigDecimal;

public record CreateTransactionRequest(
    String userId,
    BigDecimal amount,
    String currency,
    String description,
    String category,
    String transactionDate
) {
    public static CreateTransactionRequest from(String userId, BigDecimal amount, String currency, String description, String category, String transactionDate) {
        return new CreateTransactionRequest(userId, amount, currency, description, category, transactionDate);
    }
}
