package com.lazyledger.ledger.infrastructure.persistance.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "ledger_group_mappings")
@IdClass(LedgerGroupMappingId.class)
public class LedgerGroupMappingDbo {

    @Id
    @Column(name = "ledger_id")
    private UUID ledgerId;

    @Id
    @Column(name = "group_id")
    private UUID groupId;

    // Default constructor
    public LedgerGroupMappingDbo() {}

    // Constructor
    public LedgerGroupMappingDbo(UUID ledgerId, UUID groupId) {
        this.ledgerId = ledgerId;
        this.groupId = groupId;
    }

    // Getters and setters
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
}