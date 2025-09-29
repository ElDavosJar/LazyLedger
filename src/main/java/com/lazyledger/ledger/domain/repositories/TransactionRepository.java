package com.lazyledger.ledger.domain.repositories;

import com.lazyledger.commons.identifiers.LedgerId;
import com.lazyledger.commons.identifiers.TransactionId;
import com.lazyledger.ledger.domain.entities.transaction.domain.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository {

    Transaction save(Transaction transaction);

    Optional<Transaction> findById(TransactionId id);

    List<Transaction> findAll();

    List<Transaction> findByLedgerId(LedgerId ledgerId);

    List<Transaction> findByLedgerIdAndDateRange(LedgerId ledgerId, LocalDate startDate, LocalDate endDate);

    void delete(TransactionId id);
}