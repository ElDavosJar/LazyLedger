package com.lazyledger.ledger.domain.entities;

import com.lazyledger.commons.identifiers.LedgerGroupId;
import com.lazyledger.commons.identifiers.LedgerId;

import java.util.Objects;

public class LedgerGroupMapping {
    private final LedgerId ledgerId;
    private final LedgerGroupId groupId;

    private LedgerGroupMapping(LedgerId ledgerId, LedgerGroupId groupId) {
        this.ledgerId = Objects.requireNonNull(ledgerId, "LedgerId must be non-null");
        this.groupId = Objects.requireNonNull(groupId, "LedgerGroupId must be non-null");
    }

    public static LedgerGroupMapping create(LedgerId ledgerId, LedgerGroupId groupId) {
        return new LedgerGroupMapping(ledgerId, groupId);
    }

    public static LedgerGroupMapping rehydrate(LedgerId ledgerId, LedgerGroupId groupId) {
        return new LedgerGroupMapping(ledgerId, groupId);
    }

    public LedgerId getLedgerId() {
        return ledgerId;
    }

    public LedgerGroupId getGroupId() {
        return groupId;
    }

    @Override
    public String toString() {
        return "{" +
                "\n  \"ledgerId\": \"" + ledgerId + "\"," +
                "\n  \"groupId\": \"" + groupId + "\"" +
                "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LedgerGroupMapping that = (LedgerGroupMapping) o;
        return ledgerId.equals(that.ledgerId) && groupId.equals(that.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ledgerId, groupId);
    }
}