package com.lazyledger.ledger.domain.entities;

import com.lazyledger.commons.enums.LedgerStatus;
import com.lazyledger.commons.identifiers.LedgerId;
import com.lazyledger.commons.identifiers.UserId;
import com.lazyledger.ledger.domain.entities.vo.LedgerName;

import java.time.Instant;
import java.util.Objects;

public class Ledger {
    private final LedgerId id;
    private final UserId createdBy;
    private final LedgerName name;
    private final LedgerStatus status;
    private final Instant createdAt;

    private Ledger(LedgerId id, UserId createdBy, LedgerName name, LedgerStatus status, Instant createdAt) {
        this.id = Objects.requireNonNull(id, "Ledger id cannot be null");
        this.createdBy = Objects.requireNonNull(createdBy, "Created by id cannot be null");
        this.name = Objects.requireNonNull(name, "Ledger name cannot be null");
        this.status = Objects.requireNonNull(status, "Ledger status cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "Created at timestamp cannot be null");
    }

    public static Ledger create(LedgerId id, UserId createdBy, LedgerName name) {
        return new Ledger(id, createdBy, name, LedgerStatus.ACTIVE, Instant.now());
    }
    public static Ledger rehydrate(LedgerId id, UserId createdBy, LedgerName name, LedgerStatus status, Instant createdAt) {
        return new Ledger(id, createdBy, name, status, createdAt);
    }

    public LedgerId getId() {
        return id;
    }

    public Ledger createDeactivatedLedger() {
        if (this.status != LedgerStatus.ACTIVE) {
            throw new IllegalStateException("Only active ledgers can be deactivated.");
        }
        return new Ledger(this.id, this.createdBy, this.name, LedgerStatus.INACTIVE, this.createdAt);
    }
    public Ledger createArchivedLedger() {
        if (this.status != LedgerStatus.INACTIVE) {
            throw new IllegalStateException("Only inactive ledgers can be archived.");
        }
        return new Ledger(this.id, this.createdBy, this.name, LedgerStatus.ARCHIVED, this.createdAt);
    }

    public Ledger createActivatedLedger() {
        if (this.status != LedgerStatus.INACTIVE) {
            throw new IllegalStateException("Only inactive ledgers can be activated.");
        }
        return new Ledger(this.id, this.createdBy, this.name, LedgerStatus.ACTIVE, this.createdAt);
    }

    public Ledger createUnarchivedLedger() {
        if (this.status != LedgerStatus.ARCHIVED) {
            throw new IllegalStateException("Only archived ledgers can be unarchived.");
        }
        return new Ledger(this.id, this.createdBy, this.name, LedgerStatus.INACTIVE, this.createdAt);
    }

    public Ledger rename(LedgerName newName) {
        if (this.name.isSame(newName)) {
            throw new IllegalArgumentException("The new name must be different from the current name.");
        }
        return new Ledger(this.id, this.createdBy, newName, this.status, this.createdAt);
    }
    
    public UserId getCreatedBy() {
        return createdBy;
    }

    public LedgerName getName() {
        return name;
    }

    public LedgerStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    //json style con saltos de linea
    public String toString() {
        return "{" +
                "\n\"id\":\"" + id + '\"' +
                ", \n\"createdBy\":\"" + createdBy + '\"' +
                ", \n\"name\":\"" + name + '\"' +
                ", \n\"status\":\"" + status + '\"' +
                ", \n\"createdAt\":\"" + createdAt + '\"' +
                "\n}";
    }
    //compare only by id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ledger ledger = (Ledger) o;
        return id.equals(ledger.id);
    }
    
    public static void main(String[] args) {
        LedgerId ledgerId = LedgerId.of(java.util.UUID.randomUUID());
        UserId userId = UserId.of(java.util.UUID.randomUUID());
        LedgerName ledgerName = LedgerName.of("My First Ledger");

        Ledger ledger = Ledger.create(ledgerId, userId, ledgerName);
        System.out.println("\nOriginal ledger: " + ledger);

        ledger = ledger.createDeactivatedLedger();
        System.out.println("After deactivation: " + ledger);

        ledger = ledger.createActivatedLedger();
        System.out.println("After reactivation: " + ledger);

        ledger = ledger.rename(LedgerName.of("My Renamed Ledger"));
        System.out.println("After renaming: " + ledger);

        ledger = ledger.createDeactivatedLedger();
        ledger = ledger.createArchivedLedger();
        System.out.println("After archiving: " + ledger);

        ledger = ledger.createUnarchivedLedger();
        System.out.println("After unarchiving: " + ledger);
    }

    
}
