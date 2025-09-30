package com.lazyledger.ledger.application;

import com.lazyledger.commons.enums.Category;
import com.lazyledger.commons.exceptions.LedgerNotFoundException;
import com.lazyledger.commons.exceptions.TransactionNotFoundException;
import com.lazyledger.commons.identifiers.LedgerId;
import com.lazyledger.commons.identifiers.TransactionId;
import com.lazyledger.ledger.domain.entities.transaction.domain.*;
import com.lazyledger.ledger.domain.repositories.LedgerRepository;
import com.lazyledger.ledger.domain.repositories.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ManageLedgerTransactionsUseCase {

    private final TransactionRepository transactionRepository;
    private final LedgerRepository ledgerRepository;

    public ManageLedgerTransactionsUseCase(TransactionRepository transactionRepository,
                                         LedgerRepository ledgerRepository) {
        this.transactionRepository = transactionRepository;
        this.ledgerRepository = ledgerRepository;
    }

    // Validate that a ledger exists
    private void validateLedgerExists(LedgerId ledgerId) {
        if (ledgerRepository.findById(ledgerId).isEmpty()) {
            throw new LedgerNotFoundException(ledgerId.value().toString());
        }
    }

    // Create a new transaction for a ledger
    public Transaction createTransaction(LedgerId ledgerId, BigDecimal amount, String currency,
                                       String description, Category category, LocalDate transactionDate) {
        validateLedgerExists(ledgerId);

        TransactionId id = TransactionId.of(UUID.randomUUID());
        Amount transactionAmount = Amount.of(amount, currency);
        Description transactionDescription = description != null ? Description.of(description) : null;
        TransactionDate txDate = TransactionDate.of(transactionDate);

        Transaction transaction = Transaction.of(id, ledgerId, transactionAmount,
                                               transactionDescription, category, txDate);

        return transactionRepository.save(transaction);
    }

    // Find transaction by ID
    public Optional<Transaction> findTransactionById(TransactionId id) {
        return transactionRepository.findById(id);
    }

    // Find all transactions for a ledger
    public List<Transaction> findTransactionsByLedger(LedgerId ledgerId) {
        validateLedgerExists(ledgerId);
        return transactionRepository.findByLedgerId(ledgerId);
    }

    // Find transactions by ledger and date range
    public List<Transaction> findTransactionsByLedgerAndDateRange(LedgerId ledgerId,
                                                                 LocalDate startDate,
                                                                 LocalDate endDate) {
        validateLedgerExists(ledgerId);
        return transactionRepository.findByLedgerIdAndDateRange(ledgerId, startDate, endDate);
    }

    // Update transaction (create new instance with updated data)
    public Transaction updateTransaction(TransactionId id, BigDecimal amount, String currency,
                                       String description, Category category, LocalDate transactionDate) {
        Optional<Transaction> existing = transactionRepository.findById(id);
        if (existing.isEmpty()) {
            throw new TransactionNotFoundException(id.value().toString());
        }

        Transaction oldTransaction = existing.get();
        Amount newAmount = Amount.of(amount, currency);
        Description newDescription = description != null ? Description.of(description) : null;
        TransactionDate newDate = TransactionDate.of(transactionDate);

        Transaction updatedTransaction = Transaction.of(id, oldTransaction.getLedgerId(), newAmount,
                                                      newDescription, category, newDate);

        return transactionRepository.save(updatedTransaction);
    }

    // Delete transaction
    public void deleteTransaction(TransactionId id) {
        transactionRepository.delete(id);
    }

    // Get all transactions (for admin purposes)
    public List<Transaction> findAllTransactions() {
        return transactionRepository.findAll();
    }
}