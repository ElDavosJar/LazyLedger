package com.lazyledger.ledger.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Transaction information")
public record TransactionDto(
    @Schema(description = "Unique identifier of the transaction", example = "123e4567-e89b-12d3-a456-426614174000")
    String id,

    @Schema(description = "UUID of the associated ledger", example = "123e4567-e89b-12d3-a456-426614174001")
    String ledgerId,

    @Schema(description = "Transaction amount", example = "150.50")
    Number amount,

    @Schema(description = "Currency code", example = "USD")
    String currency,

    @Schema(description = "Transaction description", example = "Grocery shopping")
    String description,

    @Schema(description = "Transaction category", example = "FOOD")
    String category,

    @Schema(description = "Transaction date in ISO format", example = "2023-12-01")
    String transactionDate,

    @Schema(description = "Creation timestamp", example = "2023-12-01T10:00:00Z")
    String createdAt
) {
    public static TransactionDto from(String id, String ledgerId, Number amount, String currency,
                                    String description, String category, String transactionDate, String createdAt) {
        return new TransactionDto(id, ledgerId, amount, currency, description, category, transactionDate, createdAt);
    }
}