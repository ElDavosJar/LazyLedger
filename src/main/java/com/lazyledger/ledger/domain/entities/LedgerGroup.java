package com.lazyledger.ledger.domain.entities;

import com.lazyledger.commons.identifiers.UserId;
import com.lazyledger.commons.identifiers.LedgerGroupId;

import com.lazyledger.ledger.domain.entities.vo.LedgerName;

import java.util.Objects;

public class LedgerGroup {
    private final LedgerGroupId id;
    private final UserId ownerId;
    private final LedgerName name;

    private LedgerGroup(LedgerGroupId id, UserId ownerId, LedgerName name) {
        this.id = Objects.requireNonNull(id, "LedgerGroupId must be non-null");
        this.ownerId = Objects.requireNonNull(ownerId, "OwnerId must be non-null");
        this.name = Objects.requireNonNull(name, "LedgerName must be non-null");
    }

    // Factory method for creating a new LedgerGroup
    public static LedgerGroup create(LedgerGroupId id, UserId ownerId, LedgerName name) {
        return new LedgerGroup(id, ownerId, name);
    }
    // Rehydrate from persistence
    public static LedgerGroup rehydrate(LedgerGroupId id, UserId ownerId, LedgerName name) {
        return new LedgerGroup(id, ownerId, name);
    }

    public LedgerGroupId getId() {
        return id;
    }
    public UserId getOwnerId() {
        return ownerId;
    }
    public LedgerName getName() {
        return name;
    }
    @Override
    public String toString() {
        return "{" +
                "\n  \"id\": \"" + id + "\"," +
                "\n  \"ownerId\": \"" + ownerId + "\"," +
                "\n  \"name\": \"" + name + "\"" +
                "\n}";
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LedgerGroup that = (LedgerGroup) o;
        return id.equals(that.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, ownerId, name);
    }
}
