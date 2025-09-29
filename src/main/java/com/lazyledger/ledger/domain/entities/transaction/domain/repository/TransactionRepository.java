package com.lazyledger.ledger.domain.entities.transaction.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.lazyledger.ledger.domain.entities.transaction.domain.Transaction;

public interface TransactionRepository {
    Transaction save(Transaction transaction);
    Transaction findById(UUID id);
    List<Transaction> findAll();
    List<Transaction> findByUserIdAndDateRange(UUID userId, LocalDate startDate, LocalDate endDate);
}
