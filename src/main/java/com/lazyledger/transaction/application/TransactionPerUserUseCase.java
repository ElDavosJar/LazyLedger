package com.lazyledger.transaction.application;

import com.lazyledger.commons.UserId;
import com.lazyledger.transaction.domain.Transaction;
import com.lazyledger.transaction.domain.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionPerUserUseCase {

    private final TransactionRepository transactionRepository;

    public TransactionPerUserUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getTransactionsByUserAndDateRange(UUID userId, LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByUserIdAndDateRange(userId, startDate, endDate);
    }

    public BigDecimal calculateBalance(UUID userId, LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = getTransactionsByUserAndDateRange(userId, startDate, endDate);
        return transactions.stream()
            .map(Transaction::getAmount)
            .map(amount -> amount.value())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getMonthlyBalance(UUID userId) {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        return calculateBalance(userId, startOfMonth, now);
    }

    public BigDecimal getWeeklyBalance(UUID userId) {
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - 1);
        return calculateBalance(userId, startOfWeek, now);
    }
}