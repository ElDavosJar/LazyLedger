package com.lazyledger.transaction.infrastructure.persistance.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user_transaction_counters")
public class UserTransactionCounter {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "last_transaction_number", nullable = false)
    private Long lastTransactionNumber;

    protected UserTransactionCounter() {}

    public UserTransactionCounter(UUID userId, Long lastTransactionNumber) {
        this.userId = userId;
        this.lastTransactionNumber = lastTransactionNumber;
    }

    public UUID getUserId() {
        return userId;
    }

    public Long getLastTransactionNumber() {
        return lastTransactionNumber;
    }

    public void setLastTransactionNumber(Long lastTransactionNumber) {
        this.lastTransactionNumber = lastTransactionNumber;
    }
}