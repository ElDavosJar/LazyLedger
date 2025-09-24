package com.lazyledger.transaction.application;

import com.lazyledger.commons.TransactionId;
import com.lazyledger.commons.UserId;
import com.lazyledger.commons.enums.Category;
import com.lazyledger.trancriptionModule.TransactionDataDto;
import com.lazyledger.transaction.domain.*;
import com.lazyledger.transaction.domain.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    // Default user for external transactions
    private static final UserId DEFAULT_USER_ID = UserId.of(UUID.fromString("00000000-0000-0000-0000-000000000001"));

    public Transaction save(TransactionDataDto dto) {
        // Convert DTO to Domain Transaction
        Amount amount = Amount.of(dto.amount(), dto.currency());
        Description description = Description.of(dto.description());
        Category category = Category.valueOf(dto.category().toUpperCase());
        LocalDate date = LocalDate.parse(dto.transactionDate(), DateTimeFormatter.ISO_LOCAL_DATE);
        TransactionDate transactionDate = TransactionDate.of(date);

        TransactionId transactionId = TransactionId.of(UUID.randomUUID());

        Transaction transaction = Transaction.of(transactionId, DEFAULT_USER_ID, amount, description, category, transactionDate);

        return transactionRepository.save(transaction);
    }
}