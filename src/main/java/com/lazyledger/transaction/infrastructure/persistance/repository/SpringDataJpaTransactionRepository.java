package com.lazyledger.transaction.infrastructure.persistance.repository;

import com.lazyledger.transaction.infrastructure.persistance.entity.TransactionDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SpringDataJpaTransactionRepository extends JpaRepository<TransactionDbo, UUID> {
}
