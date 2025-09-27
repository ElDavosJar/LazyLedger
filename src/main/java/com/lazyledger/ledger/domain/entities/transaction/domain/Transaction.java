package com.lazyledger.ledger.domain.entities.transaction.domain;

import com.lazyledger.commons.TransactionId;
import com.lazyledger.commons.enums.Category;
import com.lazyledger.commons.identifiers.LedgerId;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;


public class Transaction {
    private final TransactionId id;
    private final LedgerId ledgerId;
    private final Amount amount;
    private final Description description;
    private final Category category;
    private final Instant createdAt;
    private final TransactionDate transactionDate;

    private Transaction(TransactionId id, LedgerId ledgerId, Amount amount,
                        Description description, Category category, Instant createdAt, TransactionDate transactionDate) {
        this.id = Objects.requireNonNull(id, "Transaction id must be non-null");
        this.ledgerId = Objects.requireNonNull(ledgerId, "Ledger id must be non-null");
        this.amount = Objects.requireNonNull(amount, "Amount must be non-null");
        this.description = description; // optional
        this.category = Objects.requireNonNull(category, "Category must be non-null");
        this.createdAt = Objects.requireNonNull(createdAt, "CreatedAt must be non-null");
        this.transactionDate = Objects.requireNonNull(transactionDate, "TransactionDate must be non-null");
    }

    // Factory for new transactions
    public static Transaction of(TransactionId id, LedgerId ledgerId, Amount amount,
                                  Description description, Category category, TransactionDate transactionDate) {
        return new Transaction(id, ledgerId, amount, description, category, Instant.now(), transactionDate);
    }

    // Rehydrate from persistence
    public static Transaction rehydrate(TransactionId id, LedgerId ledgerId, Amount amount,
                                        Description description, Category category, Instant createdAt, TransactionDate transactionDate) {
        return new Transaction(id, ledgerId, amount, description, category, createdAt, transactionDate);
    }

    public TransactionId getId() {
        return id;
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

    public LedgerId getLedgerId() {
        return ledgerId;
    }

    @Override
    //present simularly to JSON format for easy reading
    public String toString() {
        return "Transaction{\n" +
                "id=" + id +
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
        LedgerId ledgerId = LedgerId.of(UUID.randomUUID());
        TransactionId transactionId = TransactionId.of(UUID.randomUUID());
        Amount amount = Amount.of(BigDecimal.valueOf(100.00), "USD");
        Description description = Description.of("Grocery shopping");
        Category category = Category.FOOD;
        TransactionDate transactionDate = TransactionDate.of(null);

        Transaction transaction = Transaction.of(transactionId, ledgerId, amount, description, category, transactionDate);
        System.out.println(transaction);
    }
}
