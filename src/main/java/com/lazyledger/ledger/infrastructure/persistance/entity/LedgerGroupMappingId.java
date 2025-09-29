package com.lazyledger.ledger.infrastructure.persistance.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class LedgerGroupMappingId implements Serializable {
    private UUID ledgerId;
    private UUID groupId;

    public LedgerGroupMappingId() {}

    public LedgerGroupMappingId(UUID ledgerId, UUID groupId) {
        this.ledgerId = ledgerId;
        this.groupId = groupId;
    }

    public UUID getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(UUID ledgerId) {
        this.ledgerId = ledgerId;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LedgerGroupMappingId that = (LedgerGroupMappingId) o;
        return Objects.equals(ledgerId, that.ledgerId) && Objects.equals(groupId, that.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ledgerId, groupId);
    }
}