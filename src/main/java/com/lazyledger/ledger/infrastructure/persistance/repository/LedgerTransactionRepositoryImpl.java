package com.lazyledger.ledger.infrastructure.persistance.repository;

import com.lazyledger.commons.enums.Category;
import com.lazyledger.commons.exceptions.DatabaseException;
import com.lazyledger.commons.identifiers.LedgerId;
import com.lazyledger.commons.identifiers.TransactionId;
import com.lazyledger.ledger.domain.entities.transaction.domain.*;
import com.lazyledger.ledger.domain.repositories.TransactionRepository;
import com.lazyledger.ledger.infrastructure.persistance.entity.TransactionDbo;
import com.lazyledger.ledger.infrastructure.persistance.postgres.SpringDataJpaLedgerTransactionRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class LedgerTransactionRepositoryImpl implements TransactionRepository {

    private final SpringDataJpaLedgerTransactionRepository jpaRepository;

    public LedgerTransactionRepositoryImpl(SpringDataJpaLedgerTransactionRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        try {
            TransactionDbo dbo = toDbo(transaction);
            TransactionDbo saved = jpaRepository.save(dbo);
            return toDomain(saved);
        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to save transaction", e);
        }
    }

    @Override
    public Optional<Transaction> findById(TransactionId id) {
        try {
            Optional<TransactionDbo> dboOpt = jpaRepository.findById(id.value());
            return dboOpt.map(this::toDomain);
        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to find transaction by ID: " + id.value(), e);
        }
    }

    @Override
    public List<Transaction> findAll() {
        try {
            return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to find all transactions", e);
        }
    }

    @Override
    public List<Transaction> findByLedgerId(LedgerId ledgerId) {
        try {
            return jpaRepository.findByLedgerId(ledgerId.value()).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to find transactions by ledger ID: " + ledgerId.value(), e);
        }
    }

    @Override
    public List<Transaction> findByLedgerIdAndDateRange(LedgerId ledgerId, LocalDate startDate, LocalDate endDate) {
        try {
            return jpaRepository.findByLedgerIdAndTransactionDateBetween(ledgerId.value(), startDate, endDate).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to find transactions by ledger ID and date range: " + ledgerId.value(), e);
        }
    }

    @Override
    public void delete(TransactionId id) {
        try {
            jpaRepository.deleteById(id.value());
        } catch (DataAccessException e) {
            throw new DatabaseException("Failed to delete transaction: " + id.value(), e);
        }
    }

    private TransactionDbo toDbo(Transaction transaction) {
        return new TransactionDbo(
            transaction.getId().value(),
            transaction.getLedgerId().value(),
            transaction.getAmount().value(),
            transaction.getAmount().currency(),
            transaction.getDescription() != null ? transaction.getDescription().text() : null,
            transaction.getCategory().toString(),
            transaction.getCreatedAt(),
            transaction.getTransactionDate().value()
        );
    }

    private Transaction toDomain(TransactionDbo dbo) {
        TransactionId id = TransactionId.of(dbo.getId());
        LedgerId ledgerId = LedgerId.of(dbo.getLedgerId());
        Amount amount = Amount.of(dbo.getAmountValue(), dbo.getCurrency());
        Description description = dbo.getDescription() != null ? Description.of(dbo.getDescription()) : null;
        Category category = Category.fromString(dbo.getCategory());
        TransactionDate transactionDate = TransactionDate.of(dbo.getTransactionDate());
        return Transaction.of(id, ledgerId, amount, description, category, transactionDate);
    }
}