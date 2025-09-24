package com.lazyledger.transaction.api.dto;


public record TransactionDto(
    String id,
    String userId,
    Number amount,
    String currency,
    String description,
    String category,
    String transactionDate,
    String createdAt
) {
    public static TransactionDto from(String id, String userId, Number amount, String currency, String description, String category, String transactionDate, String createdAt) {
        return new TransactionDto(id, userId, amount, currency, description, category, transactionDate, createdAt);
    }
}
