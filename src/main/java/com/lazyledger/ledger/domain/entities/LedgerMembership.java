package com.lazyledger.ledger.domain.entities;
import com.lazyledger.commons.identifiers.UserId;
import com.lazyledger.commons.enums.LedgerUserRole;
import com.lazyledger.commons.identifiers.LedgerId;
import java.util.Objects;

public class LedgerMembership {
    private final UserId userId;
    private final LedgerId ledgerId;
    private final LedgerUserRole role;

    private LedgerMembership(UserId userId, LedgerId ledgerId, LedgerUserRole role) {
        this.userId = Objects.requireNonNull(userId, "UserId must be non-null");
        this.ledgerId = Objects.requireNonNull(ledgerId, "LedgerId must be non-null");
        this.role = Objects.requireNonNull(role, "Role must be non-null");
    }
    // Factory method
    public static LedgerMembership create(UserId userId, LedgerId ledgerId, LedgerUserRole role) {
        return new LedgerMembership(userId, ledgerId, role);
    }
    public static LedgerMembership rehydrate(UserId userId, LedgerId ledgerId, LedgerUserRole role) {
        return new LedgerMembership(userId, ledgerId, role);
    }

    public UserId getUserId() {
        return userId;
    }
    public LedgerId getLedgerId() {
        return ledgerId;
    }
    public LedgerUserRole getRole() {
        return role;
    }
    //format like Json
    @Override
    public String toString() {
        return "{" +
                "\n  \"userId\": \"" + userId + "\"," +
                "\n  \"ledgerId\": \"" + ledgerId + "\"," +
                "\n  \"role\": \"" + role.getValue() + "\"" +
                "\n}";
    }

    //compare by userId and ledgerId combination only
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LedgerMembership that = (LedgerMembership) o;
        return userId.equals(that.userId) && ledgerId.equals(that.ledgerId);
    }


    @Override
    public int hashCode() {
        return Objects.hash(userId, ledgerId, role);
    }
}
