package com.lazyledger.transaction.infrastructure.persistance.repository;

import com.lazyledger.transaction.infrastructure.persistance.entity.TransactionDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SpringDataJpaTransactionRepository extends JpaRepository<TransactionDbo, UUID> {

    @Query("SELECT t FROM TransactionDbo t WHERE t.userId = :userId AND t.transactionDate BETWEEN :startDate AND :endDate")
    List<TransactionDbo> findByUserIdAndTransactionDateBetween(@Param("userId") UUID userId,
                                                               @Param("startDate") LocalDate startDate,
                                                               @Param("endDate") LocalDate endDate);
}
