package com.lazyledger.ledger.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Request to create a new transaction")
public record CreateTransactionRequest(
    @Schema(description = "UUID of the ledger", example = "123e4567-e89b-12d3-a456-426614174000", required = true)
    String ledgerId,

    @Schema(description = "Transaction amount", example = "150.50", required = true)
    BigDecimal amount,

    @Schema(description = "Currency code", example = "USD", required = true)
    String currency,

    @Schema(description = "Transaction description", example = "Grocery shopping")
    String description,

    @Schema(description = "Transaction category", example = "FOOD", required = true)
    String category,

    @Schema(description = "Transaction date in ISO format", example = "2023-12-01", required = true)
    String transactionDate
) {
    public static CreateTransactionRequest from(String ledgerId, BigDecimal amount, String currency,
                                              String description, String category, String transactionDate) {
        return new CreateTransactionRequest(ledgerId, amount, currency, description, category, transactionDate);
    }
}