package com.lazyledger.ledger.infrastructure.persistance.repository;

import com.lazyledger.commons.enums.Category;
import com.lazyledger.commons.identifiers.LedgerId;
import com.lazyledger.commons.identifiers.TransactionId;
import com.lazyledger.ledger.domain.entities.transaction.domain.*;
import com.lazyledger.ledger.domain.repositories.TransactionRepository;
import com.lazyledger.ledger.infrastructure.persistance.entity.TransactionDbo;
import com.lazyledger.ledger.infrastructure.persistance.postgres.SpringDataJpaLedgerTransactionRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class LedgerTransactionRepositoryImpl implements TransactionRepository {

    private final SpringDataJpaLedgerTransactionRepository jpaRepository;

    public LedgerTransactionRepositoryImpl(SpringDataJpaLedgerTransactionRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        TransactionDbo dbo = toDbo(transaction);
        TransactionDbo saved = jpaRepository.save(dbo);
        return toDomain(saved);
    }

    @Override
    public Optional<Transaction> findById(TransactionId id) {
        Optional<TransactionDbo> dboOpt = jpaRepository.findById(id.value());
        return dboOpt.map(this::toDomain);
    }

    @Override
    public List<Transaction> findAll() {
        return jpaRepository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findByLedgerId(LedgerId ledgerId) {
        return jpaRepository.findByLedgerId(ledgerId.value()).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findByLedgerIdAndDateRange(LedgerId ledgerId, LocalDate startDate, LocalDate endDate) {
        return jpaRepository.findByLedgerIdAndTransactionDateBetween(ledgerId.value(), startDate, endDate).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void delete(TransactionId id) {
        jpaRepository.deleteById(id.value());
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