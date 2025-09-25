package com.lazyledger.transaction.api.dto;


public record TransactionDto(
    String id,
    String userId,
    Long transactionNumber,
    Number amount,
    String currency,
    String description,
    String category,
    String transactionDate,
    String createdAt
) {
    public static TransactionDto from(String id, String userId, Long transactionNumber, Number amount, String currency, String description, String category, String transactionDate, String createdAt) {
        return new TransactionDto(id, userId, transactionNumber, amount, currency, description, category, transactionDate, createdAt);
    }
}
