package com.lazyledger.ledger.api;

import com.lazyledger.commons.enums.Category;
import com.lazyledger.commons.exceptions.InvalidInputException;
import com.lazyledger.commons.exceptions.TransactionNotFoundException;
import com.lazyledger.commons.identifiers.LedgerId;
import com.lazyledger.commons.identifiers.TransactionId;
import com.lazyledger.ledger.api.dto.CreateTransactionRequest;
import com.lazyledger.ledger.api.dto.TransactionDto;
import com.lazyledger.ledger.application.ManageLedgerTransactionsUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
        try {
            UUID ledgerUuid = UUID.fromString(request.ledgerId());
            LedgerId ledgerId = LedgerId.of(ledgerUuid);
            LocalDate txDate = LocalDate.parse(request.transactionDate());
            Category category = Category.fromString(request.category());
            var transaction = manageLedgerTransactionsUseCase.createTransaction(
                ledgerId,
                request.amount(),
                request.currency(),
                request.description(),
                category,
                txDate
            );
            return LedgerTransactionMapper.toTransactionDto(transaction);
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Invalid input data: " + e.getMessage(), e);
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("Invalid transaction date format: " + request.transactionDate(), e);
        }
    }

    public TransactionDto getTransactionById(String transactionId) {
        try {
            UUID txUuid = UUID.fromString(transactionId);
            var transactionOpt = manageLedgerTransactionsUseCase.findTransactionById(TransactionId.of(txUuid));
            if (transactionOpt.isEmpty()) {
                throw new TransactionNotFoundException(transactionId);
            }
            return LedgerTransactionMapper.toTransactionDto(transactionOpt.get());
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Invalid transaction ID format: " + transactionId, e);
        }
    }

    public List<TransactionDto> getTransactionsByLedgerId(String ledgerId) {
        try {
            UUID ledgerUuid = UUID.fromString(ledgerId);
            var transactions = manageLedgerTransactionsUseCase.findTransactionsByLedger(LedgerId.of(ledgerUuid));
            return transactions.stream()
                .map(LedgerTransactionMapper::toTransactionDto)
                .toList();
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Invalid ledger ID format: " + ledgerId, e);
        }
    }

    public List<TransactionDto> getTransactionsByLedgerIdAndDateRange(String ledgerId, String startDate, String endDate) {
        try {
            UUID ledgerUuid = UUID.fromString(ledgerId);
            LedgerId ledgerIdObj = LedgerId.of(ledgerUuid);
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            var transactions = manageLedgerTransactionsUseCase.findTransactionsByLedgerAndDateRange(
                ledgerIdObj,
                start,
                end
            );
            return transactions.stream()
                .map(LedgerTransactionMapper::toTransactionDto)
                .toList();
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Invalid ledger ID format: " + ledgerId, e);
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("Invalid date format: " + e.getMessage(), e);
        }
    }

    public void deleteTransaction(String transactionId) {
        try {
            UUID txUuid = UUID.fromString(transactionId);
            manageLedgerTransactionsUseCase.deleteTransaction(TransactionId.of(txUuid));
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Invalid transaction ID format: " + transactionId, e);
        }
    }
}