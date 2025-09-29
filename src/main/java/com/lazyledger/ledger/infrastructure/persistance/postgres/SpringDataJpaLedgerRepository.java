package com.lazyledger.ledger.infrastructure.persistance.postgres;

import com.lazyledger.ledger.infrastructure.persistance.entity.LedgerDbo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataJpaLedgerRepository extends JpaRepository<LedgerDbo, UUID> {
}