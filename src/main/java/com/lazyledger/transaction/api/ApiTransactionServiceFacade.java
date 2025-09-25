package com.lazyledger.transaction.api;

import com.lazyledger.transaction.api.dto.TransactionDto;
import com.lazyledger.transaction.application.TransactionService;
import com.lazyledger.transaction.domain.Transaction;
import com.lazyledger.transcription.extractor.TransactionDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ApiTransactionServiceFacade {

    private final TransactionService transactionService;
    @Autowired
    public ApiTransactionServiceFacade(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public TransactionDto processFile(byte[] fileBytes, String mediaType) {
        List<TransactionDto> dtos = transactionService.processFile(fileBytes, mediaType);
        return dtos.stream().findFirst().orElse(null);
    }

    public TransactionDto save(TransactionDataDto dataDto) {
        Transaction saved = transactionService.save(dataDto);
        return TransactionMapper.toTransactionDto(saved);
    }

    public List<TransactionDto> findAll() {
        List<Transaction> transactions = transactionService.findAll();
        return transactions.stream().map(TransactionMapper::toTransactionDto).toList();
    }

    public TransactionDto findById(UUID id) {
        Transaction transaction = transactionService.findById(id);
        return TransactionMapper.toTransactionDto(transaction);
    }
}