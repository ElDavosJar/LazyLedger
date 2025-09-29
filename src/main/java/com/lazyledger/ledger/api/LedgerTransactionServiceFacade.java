package com.lazyledger.ledger.api;

import com.lazyledger.commons.enums.Category;
import com.lazyledger.commons.identifiers.LedgerId;
import com.lazyledger.commons.identifiers.TransactionId;
import com.lazyledger.ledger.api.dto.CreateTransactionRequest;
import com.lazyledger.ledger.api.dto.TransactionDto;
import com.lazyledger.ledger.application.ManageLedgerTransactionsUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class LedgerTransactionServiceFacade {

    private final ManageLedgerTransactionsUseCase manageLedgerTransactionsUseCase;

    @Autowired
    public LedgerTransactionServiceFacade(ManageLedgerTransactionsUseCase manageLedgerTransactionsUseCase) {
        this.manageLedgerTransactionsUseCase = manageLedgerTransactionsUseCase;
    }

    public TransactionDto addTransaction(CreateTransactionRequest request) {
        var transaction = manageLedgerTransactionsUseCase.createTransaction(
            LedgerId.of(UUID.fromString(request.ledgerId())),
            request.amount(),
            request.currency(),
            request.description(),
            Category.fromString(request.category()),
            LocalDate.parse(request.transactionDate())
        );
        return LedgerTransactionMapper.toTransactionDto(transaction);
    }

    public TransactionDto getTransactionById(String transactionId) {
        var transactionOpt = manageLedgerTransactionsUseCase.findTransactionById(TransactionId.of(UUID.fromString(transactionId)));
        if (transactionOpt.isEmpty()) {
            throw new IllegalArgumentException("Transaction not found: " + transactionId);
        }
        return LedgerTransactionMapper.toTransactionDto(transactionOpt.get());
    }

    public List<TransactionDto> getTransactionsByLedgerId(String ledgerId) {
        var transactions = manageLedgerTransactionsUseCase.findTransactionsByLedger(LedgerId.of(UUID.fromString(ledgerId)));
        return transactions.stream()
            .map(LedgerTransactionMapper::toTransactionDto)
            .toList();
    }

    public List<TransactionDto> getTransactionsByLedgerIdAndDateRange(String ledgerId, String startDate, String endDate) {
        var transactions = manageLedgerTransactionsUseCase.findTransactionsByLedgerAndDateRange(
            LedgerId.of(UUID.fromString(ledgerId)),
            LocalDate.parse(startDate),
            LocalDate.parse(endDate)
        );
        return transactions.stream()
            .map(LedgerTransactionMapper::toTransactionDto)
            .toList();
    }

    public void deleteTransaction(String transactionId) {
        manageLedgerTransactionsUseCase.deleteTransaction(TransactionId.of(UUID.fromString(transactionId)));
    }
}