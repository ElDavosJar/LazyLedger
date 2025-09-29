package com.lazyledger.ledger.api.dto;

public record LedgerDto(
    String id,
    String createdBy,
    String name,
    String status,
    String createdAt
) {
    public static LedgerDto from(String id, String createdBy, String name, String status, String createdAt) {
        return new LedgerDto(id, createdBy, name, status, createdAt);
    }
}