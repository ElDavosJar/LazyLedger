package com.lazyledger.transaction.infrastructure.persistance.repository;

import com.lazyledger.commons.TransactionId;
import com.lazyledger.commons.UserId;
import com.lazyledger.commons.enums.Category;
import com.lazyledger.transaction.domain.*;
import com.lazyledger.transaction.domain.repository.TransactionRepository;
import com.lazyledger.transaction.infrastructure.persistance.entity.TransactionDbo;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

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

    private TransactionDbo toDbo(Transaction transaction) {
        return new TransactionDbo(
            transaction.getId().value(),
            transaction.getUserId().value(),
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
        Amount amount = Amount.of(dbo.getAmountValue(), dbo.getCurrency());
        Description description = dbo.getDescription() != null ? new Description(dbo.getDescription()) : null;
        Category category = Category.fromString(dbo.getCategory());
        TransactionDate transactionDate = new TransactionDate(dbo.getTransactionDate());
        return Transaction.rehydrate(id, userId, amount, description, category, dbo.getCreatedAt(), transactionDate);
    }
}
