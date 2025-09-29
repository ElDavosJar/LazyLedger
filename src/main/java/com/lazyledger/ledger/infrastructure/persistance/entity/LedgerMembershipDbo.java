package com.lazyledger.ledger.infrastructure.persistance.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "ledger_memberships")
@IdClass(LedgerMembershipId.class)
public class LedgerMembershipDbo {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Id
    @Column(name = "ledger_id")
    private UUID ledgerId;

    @Column(nullable = false)
    private String role;

    // Default constructor
    public LedgerMembershipDbo() {}

    // Constructor
    public LedgerMembershipDbo(UUID userId, UUID ledgerId, String role) {
        this.userId = userId;
        this.ledgerId = ledgerId;
        this.role = role;
    }

    // Getters and setters
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}