package com.lazyledger.transaction.infrastructure.persistance.repository;

import com.lazyledger.commons.TransactionId;
import com.lazyledger.commons.UserId;
import com.lazyledger.commons.enums.Category;
import com.lazyledger.transaction.domain.*;
import com.lazyledger.transaction.domain.repository.TransactionRepository;
import com.lazyledger.transaction.infrastructure.persistance.entity.TransactionDbo;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class TransactioRepositoryImpl implements TransactionRepository {

    private final SpringDataJpaTransactionRepository jpaRepository;

    public TransactioRepositoryImpl(SpringDataJpaTransactionRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        TransactionDbo dbo = toDbo(transaction);
        TransactionDbo saved = jpaRepository.save(dbo);
        return toDomain(saved);
    }

    @Override
    public Transaction findById(UUID id) {
        Optional<TransactionDbo> dboOpt = jpaRepository.findById(id);
        return dboOpt.map(this::toDomain).orElse(null);
    }

    @Override
    public List<Transaction> findAll() {
        return jpaRepository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findByUserIdAndDateRange(UUID userId, LocalDate startDate, LocalDate endDate) {
        return jpaRepository.findByUserIdAndTransactionDateBetween(userId, startDate, endDate).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    private TransactionDbo toDbo(Transaction transaction) {
        return new TransactionDbo(
            transaction.getId().value(),
            transaction.getUserId().value(),
            transaction.getTransactionNumber(),
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
        UserId userId = UserId.of(dbo.getUserId());
        Long transactionNumber = dbo.getTransactionNumber();
        Amount amount = Amount.of(dbo.getAmountValue(), dbo.getCurrency());
        Description description = dbo.getDescription() != null ? new Description(dbo.getDescription()) : null;
        Category category = Category.fromString(dbo.getCategory());
        TransactionDate transactionDate = new TransactionDate(dbo.getTransactionDate());
        return Transaction.rehydrate(id, userId, transactionNumber, amount, description, category, dbo.getCreatedAt(), transactionDate);
    }
}
