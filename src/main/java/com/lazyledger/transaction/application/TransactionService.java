package com.lazyledger.transaction.application;

import com.lazyledger.commons.TransactionId;
import com.lazyledger.commons.UserId;
import com.lazyledger.commons.enums.Category;
import com.lazyledger.commons.exceptions.TransactionNotFoundException;
import com.lazyledger.transcription.extractor.TransactionDataDto;
import com.lazyledger.transcription.transcribers.TranscriptionService;
import com.lazyledger.transaction.api.TransactionMapper;
import com.lazyledger.transaction.api.dto.TransactionDto;
import com.lazyledger.transaction.domain.*;
import com.lazyledger.transaction.domain.repository.TransactionRepository;
import com.lazyledger.transaction.infrastructure.persistance.entity.UserTransactionCounter;
import com.lazyledger.transaction.infrastructure.persistance.repository.UserTransactionCounterRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TranscriptionService transcriptionService;
    private final UserTransactionCounterRepository counterRepository;

    // Default user for external transactions
    private static final UserId DEFAULT_USER_ID = UserId.of(UUID.fromString("00000000-0000-0000-0000-000000000001"));

    public TransactionService(TransactionRepository transactionRepository, TranscriptionService transcriptionService, UserTransactionCounterRepository counterRepository) {
        this.transactionRepository = transactionRepository;
        this.transcriptionService = transcriptionService;
        this.counterRepository = counterRepository;
    }

    public Transaction save(TransactionDataDto dto) {
        // Convert DTO to Domain Transaction
        Amount amount = Amount.of(dto.amount(), dto.currency());
        Description description = Description.of(dto.description());
        Category category = Category.valueOf(dto.category().toUpperCase());
        LocalDate date = LocalDate.parse(dto.transactionDate(), DateTimeFormatter.ISO_LOCAL_DATE);
        TransactionDate transactionDate = TransactionDate.of(date);

        TransactionId transactionId = TransactionId.of(UUID.randomUUID());
        Long transactionNumber = getNextTransactionNumber(DEFAULT_USER_ID);

        Transaction transaction = Transaction.of(transactionId, DEFAULT_USER_ID, transactionNumber, amount, description, category, transactionDate);

        return transactionRepository.save(transaction);
    }

    private Long getNextTransactionNumber(UserId userId) {
        return counterRepository.findByUserIdForUpdate(userId.value())
            .map(counter -> {
                Long next = counter.getLastTransactionNumber() + 1;
                counter.setLastTransactionNumber(next);
                counterRepository.save(counter);
                return next;
            })
            .orElseGet(() -> {
                // First transaction for this user
                UserTransactionCounter counter = new UserTransactionCounter(userId.value(), 1L);
                counterRepository.save(counter);
                return 1L;
            });
    }

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public Transaction findById(UUID id) {
        Transaction transaction = transactionRepository.findById(id);
        if (transaction == null) {
            throw new TransactionNotFoundException(id.toString());
        }
        return transaction;
    }

    @Transactional
    public List<TransactionDto> processFile(byte[] fileBytes, String mediaType) {
        List<TransactionDataDto> extracted = transcriptionService.transcribeAndExtractTransactions(fileBytes, mediaType);
        return extracted.stream()
            .map(this::save)
            .map(TransactionMapper::toTransactionDto)
            .toList();
    }
}