package com.lazyledger.ledger.infrastructure.persistance.postgres;

import com.lazyledger.ledger.infrastructure.persistance.entity.LedgerMembershipDbo;
import com.lazyledger.ledger.infrastructure.persistance.entity.LedgerMembershipId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SpringDataJpaLedgerMembershipRepository extends JpaRepository<LedgerMembershipDbo, LedgerMembershipId> {

    @Query("SELECT lm FROM LedgerMembershipDbo lm WHERE lm.userId = :userId AND lm.ledgerId = :ledgerId")
    LedgerMembershipDbo findByUserIdAndLedgerId(@Param("userId") UUID userId, @Param("ledgerId") UUID ledgerId);

    @Query("SELECT lm FROM LedgerMembershipDbo lm WHERE lm.ledgerId = :ledgerId")
    List<LedgerMembershipDbo> findAllByLedgerId(@Param("ledgerId") UUID ledgerId);

    @Query("SELECT lm FROM LedgerMembershipDbo lm WHERE lm.userId = :userId")
    List<LedgerMembershipDbo> findAllByUserId(@Param("userId") UUID userId);
}