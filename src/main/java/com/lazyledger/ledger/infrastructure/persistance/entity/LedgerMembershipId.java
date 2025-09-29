package com.lazyledger.ledger.infrastructure.persistance.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class LedgerMembershipId implements Serializable {
    private UUID userId;
    private UUID ledgerId;

    public LedgerMembershipId() {}

    public LedgerMembershipId(UUID userId, UUID ledgerId) {
        this.userId = userId;
        this.ledgerId = ledgerId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(UUID ledgerId) {
        this.ledgerId = ledgerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LedgerMembershipId that = (LedgerMembershipId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(ledgerId, that.ledgerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, ledgerId);
    }
}