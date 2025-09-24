package com.lazyledger.transaction.domain.repository;

import java.util.UUID;

import com.lazyledger.transaction.domain.Transaction;

public interface TransactionRepository {
    Transaction save(Transaction transaction);
    Transaction findById(UUID id);
}
