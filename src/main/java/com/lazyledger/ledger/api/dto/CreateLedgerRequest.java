package com.lazyledger.ledger.api.dto;

public record CreateLedgerRequest(
    String name,
    String userId,
    String groupId,
    String groupName
) {
    public static CreateLedgerRequest from(String name, String userId) {
        return new CreateLedgerRequest(name, userId, null, null);
    }

    public static CreateLedgerRequest from(String name, String userId, String groupId, String groupName) {
        return new CreateLedgerRequest(name, userId, groupId, groupName);
    }
}