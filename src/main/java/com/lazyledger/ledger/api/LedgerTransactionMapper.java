package com.lazyledger.ledger.api;

import com.lazyledger.ledger.api.dto.TransactionDto;
import com.lazyledger.ledger.domain.entities.transaction.domain.Transaction;

public class LedgerTransactionMapper {

    public static TransactionDto toTransactionDto(Transaction transaction) {
        return TransactionDto.from(
            transaction.getId().toString(),
            transaction.getLedgerId().toString(),
            transaction.getAmount().value(),
            transaction.getAmount().currency(),
            transaction.getDescription() != null ? transaction.getDescription().text() : null,
            transaction.getCategory().toString(),
            transaction.getTransactionDate().value().toString(),
            transaction.getCreatedAt().toString()
        );
    }
}