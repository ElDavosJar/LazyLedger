package com.lazyledger.ledger.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to create a new ledger")
public record CreateLedgerRequest(
    @Schema(description = "Name of the ledger", example = "My Personal Ledger", required = true)
    String name,

    @Schema(description = "UUID of the user creating the ledger", example = "123e4567-e89b-12d3-a456-426614174000", required = true)
    String userId,

    @Schema(description = "UUID of existing group (optional)", example = "123e4567-e89b-12d3-a456-426614174001")
    String groupId,

    @Schema(description = "Name for new group (optional)", example = "Family Finances")
    String groupName
) {
    public static CreateLedgerRequest from(String name, String userId) {
        return new CreateLedgerRequest(name, userId, null, null);
    }

    public static CreateLedgerRequest from(String name, String userId, String groupId, String groupName) {
        return new CreateLedgerRequest(name, userId, groupId, groupName);
    }
}