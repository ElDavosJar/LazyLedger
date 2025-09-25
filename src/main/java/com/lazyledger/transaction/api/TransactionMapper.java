package com.lazyledger.transaction.api;

import com.lazyledger.transaction.api.dto.CreateTransactionRequest;
import com.lazyledger.transaction.api.dto.TransactionDto;
import com.lazyledger.transaction.domain.*;
import com.lazyledger.transcription.extractor.TransactionDataDto;

public class TransactionMapper {

    public static TransactionDataDto toTransactionDataDto(CreateTransactionRequest request) {
        return new TransactionDataDto(
            request.amount(),
            request.currency(),
            request.description(),
            request.category(),
            request.transactionDate()
        );
    }

    public static TransactionDto toTransactionDto(Transaction transaction) {
        return new TransactionDto(
            transaction.getId().value().toString(),
            transaction.getUserId().value().toString(),
            transaction.getTransactionNumber(),
            transaction.getAmount().value(),
            transaction.getAmount().currency(),
            transaction.getDescription() != null ? transaction.getDescription().text() : null,
            transaction.getCategory().toString(),
            transaction.getTransactionDate().value().toString(),
            transaction.getCreatedAt().toString()
        );
    }
}