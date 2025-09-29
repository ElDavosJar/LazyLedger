package com.lazyledger.ledger.infrastructure.persistance.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "ledger_groups")
public class LedgerGroupDbo {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID ownerId;

    @Column(nullable = false)
    private String name;

    // Default constructor
    public LedgerGroupDbo() {}

    // Constructor
    public LedgerGroupDbo(UUID id, UUID ownerId, String name) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}