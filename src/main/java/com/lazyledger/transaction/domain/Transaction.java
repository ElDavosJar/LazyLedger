package com.lazyledger.transaction.domain;

import com.lazyledger.commons.TransactionId;
import com.lazyledger.commons.UserId;
import com.lazyledger.commons.enums.Category;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;


public class Transaction {
    private final TransactionId id;
    private final UserId userId;
    private final Long transactionNumber;
    private final Amount amount;
    private final Description description;
    private final Category category;
    private final Instant createdAt;
    private final TransactionDate transactionDate;

    private Transaction(TransactionId id, UserId userId, Long transactionNumber, Amount amount,
                        Description description, Category category, Instant createdAt, TransactionDate transactionDate) {
        this.id = Objects.requireNonNull(id, "Transaction id must be non-null");
        this.userId = Objects.requireNonNull(userId, "UserId must be non-null");
        this.transactionNumber = Objects.requireNonNull(transactionNumber, "TransactionNumber must be non-null");
        this.amount = Objects.requireNonNull(amount, "Amount must be non-null");
        this.description = description; // optional
        this.category = Objects.requireNonNull(category, "Category must be non-null");
        this.createdAt = Objects.requireNonNull(createdAt, "CreatedAt must be non-null");
        this.transactionDate = Objects.requireNonNull(transactionDate, "TransactionDate must be non-null");
    }

    // Factory for new transactions
    public static Transaction of(TransactionId id, UserId userId, Long transactionNumber, Amount amount,
                                 Description description, Category category, TransactionDate transactionDate) {
        return new Transaction(id, userId, transactionNumber, amount, description, category, Instant.now(), transactionDate);
    }

    // Rehydrate from persistence
    public static Transaction rehydrate(TransactionId id, UserId userId, Long transactionNumber, Amount amount,
                                        Description description, Category category, Instant createdAt, TransactionDate transactionDate) {
        return new Transaction(id, userId, transactionNumber, amount, description, category, createdAt, transactionDate);
    }

    public TransactionId getId() {
        return id;
    }

    public UserId getUserId() {
        return userId;
    }

    public Long getTransactionNumber() {
        return transactionNumber;
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
    //present simularly to JSON format for easy reading
    public String toString() {
        return "Transaction{\n" +
                "id=" + id +
                ",\n userId=" + userId +
                ",\n transactionNumber=" + transactionNumber +
                ",\n amount=" + amount +
                ",\n description=" + description +
                ",\n category=" + category +
                ",\n createdAt=" + createdAt +
                ",\n transactionDate=" + transactionDate +
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

    public static void main(String[] args) {
        UserId userId = UserId.of(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        TransactionId transactionId = TransactionId.of(UUID.randomUUID());
        Amount amount = Amount.of(BigDecimal.valueOf(100.00), "USD");
        Description description = Description.of("Grocery shopping");
        Category category = Category.FOOD;
        TransactionDate transactionDate = TransactionDate.of(null);

        Transaction transaction = Transaction.of(transactionId, userId, 1L, amount, description, category, transactionDate);
        System.out.println(transaction);
    }
}
