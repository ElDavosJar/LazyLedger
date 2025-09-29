package com.lazyledger.ledger.infrastructure.persistance.postgres;

import com.lazyledger.ledger.infrastructure.persistance.entity.TransactionDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SpringDataJpaLedgerTransactionRepository extends JpaRepository<TransactionDbo, UUID> {

    @Query("SELECT t FROM TransactionDbo t WHERE t.ledgerId = :ledgerId")
    List<TransactionDbo> findByLedgerId(@Param("ledgerId") UUID ledgerId);

    @Query("SELECT t FROM TransactionDbo t WHERE t.ledgerId = :ledgerId AND t.transactionDate BETWEEN :startDate AND :endDate")
    List<TransactionDbo> findByLedgerIdAndTransactionDateBetween(@Param("ledgerId") UUID ledgerId,
                                                               @Param("startDate") LocalDate startDate,
                                                               @Param("endDate") LocalDate endDate);
}