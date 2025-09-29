package com.lazyledger.ledger.infrastructure.persistance.entity;

import com.lazyledger.commons.enums.Category;
import com.lazyledger.commons.identifiers.LedgerId;
import com.lazyledger.commons.identifiers.TransactionId;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class TransactionDbo {

    @Id
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;

    @Column(name = "ledger_id", columnDefinition = "UUID", nullable = false)
    private UUID ledgerId;

    @Column(name = "amount_value", precision = 19, scale = 2, nullable = false)
    private BigDecimal amountValue;

    @Column(name = "currency", length = 3, nullable = false)
    private String currency;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    protected TransactionDbo() {
        // JPA default constructor
    }

    public TransactionDbo(UUID id, UUID ledgerId, BigDecimal amountValue, String currency,
                         String description, String category, Instant createdAt, LocalDate transactionDate) {
        this.id = id;
        this.ledgerId = ledgerId;
        this.amountValue = amountValue;
        this.currency = currency;
        this.description = description;
        this.category = category;
        this.createdAt = createdAt;
        this.transactionDate = transactionDate;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public UUID getLedgerId() {
        return ledgerId;
    }

    public BigDecimal getAmountValue() {
        return amountValue;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    // Setters
    public void setId(UUID id) {
        this.id = id;
    }

    public void setLedgerId(UUID ledgerId) {
        this.ledgerId = ledgerId;
    }

    public void setAmountValue(BigDecimal amountValue) {
        this.amountValue = amountValue;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }
}