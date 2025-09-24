package com.lazyledger.transaction.infrastructure.persistance.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class TransactionDbo {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private BigDecimal amountValue;

    @Column(nullable = false)
    private String currency;

    private String description;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private LocalDate transactionDate;

    // Default constructor
    public TransactionDbo() {}

    // Constructor
    public TransactionDbo(UUID id, UUID userId, BigDecimal amountValue, String currency, String description, String category, Instant createdAt, LocalDate transactionDate) {
        this.id = id;
        this.userId = userId;
        this.amountValue = amountValue;
        this.currency = currency;
        this.description = description;
        this.category = category;
        this.createdAt = createdAt;
        this.transactionDate = transactionDate;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public BigDecimal getAmountValue() {
        return amountValue;
    }

    public void setAmountValue(BigDecimal amountValue) {
        this.amountValue = amountValue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

}
