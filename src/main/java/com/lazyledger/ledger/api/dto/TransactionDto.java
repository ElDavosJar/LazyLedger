package com.lazyledger.ledger.api.dto;

public record TransactionDto(
    String id,
    String ledgerId,
    Number amount,
    String currency,
    String description,
    String category,
    String transactionDate,
    String createdAt
) {
    public static TransactionDto from(String id, String ledgerId, Number amount, String currency,
                                    String description, String category, String transactionDate, String createdAt) {
        return new TransactionDto(id, ledgerId, amount, currency, description, category, transactionDate, createdAt);
    }
}