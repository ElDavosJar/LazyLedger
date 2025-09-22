package com.lazyledger.transaction.domain;

import com.lazyledger.commons.TransactionId;
import com.lazyledger.commons.UserId;
import com.lazyledger.commons.enums.Category;

import java.time.Instant;
import java.util.Objects;

public class Transaction {
    private final TransactionId id;
    private final UserId userId;
    private final Amount amount;
    private final Description description;
    private final Category category;
    private final Instant createdAt;
    private final TransactionDate transactionDate;

    private Transaction(TransactionId id, UserId userId, Amount amount,
                        Description description, Category category, Instant createdAt, TransactionDate transactionDate) {
        this.id = Objects.requireNonNull(id, "Transaction id must be non-null");
        this.userId = Objects.requireNonNull(userId, "UserId must be non-null");
        this.amount = Objects.requireNonNull(amount, "Amount must be non-null");
        this.description = description; // optional
        this.category = Objects.requireNonNull(category, "Category must be non-null");
        this.createdAt = Objects.requireNonNull(createdAt, "CreatedAt must be non-null");
        this.transactionDate = Objects.requireNonNull(transactionDate, "TransactionDate must be non-null");
    }

    // Factory for new transactions
    public static Transaction of(TransactionId id, UserId userId, Amount amount,
                                 Description description, Category category, TransactionDate transactionDate) {
        return new Transaction(id, userId, amount, description, category, Instant.now(), transactionDate);
    }

    // Rehydrate from persistence
    public static Transaction rehydrate(TransactionId id, UserId userId, Amount amount,
                                        Description description, Category category, Instant createdAt, TransactionDate transactionDate) {
        return new Transaction(id, userId, amount, description, category, createdAt, transactionDate);
    }

    public TransactionId getId() {
        return id;
    }

    public UserId getUserId() {
        return userId;
    }

    public Amount getAmount() {
        return amount;
    }

    public Description getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public TransactionDate getTransactionDate() {
        return transactionDate;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", userId=" + userId +
                ", amount=" + amount +
                ", description=" + description +
                ", category=" + category +
                ", createdAt=" + createdAt +
                ", transactionDate=" + transactionDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction that)) return false;
        return id.equals(that.id); //Only id is used for equality
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
