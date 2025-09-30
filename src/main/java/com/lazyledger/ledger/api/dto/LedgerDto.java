package com.lazyledger.ledger.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ledger information")
public record LedgerDto(
    @Schema(description = "Unique identifier of the ledger", example = "123e4567-e89b-12d3-a456-426614174000")
    String id,

    @Schema(description = "UUID of the user who created the ledger", example = "123e4567-e89b-12d3-a456-426614174001")
    String createdBy,

    @Schema(description = "Name of the ledger", example = "My Personal Ledger")
    String name,

    @Schema(description = "Current status of the ledger", example = "ACTIVE")
    String status,

    @Schema(description = "Creation timestamp", example = "2023-12-01T10:00:00Z")
    String createdAt
) {
    public static LedgerDto from(String id, String createdBy, String name, String status, String createdAt) {
        return new LedgerDto(id, createdBy, name, status, createdAt);
    }
}