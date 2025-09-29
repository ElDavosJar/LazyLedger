package com.lazyledger.ledger.infrastructure.persistance.postgres;

import com.lazyledger.ledger.infrastructure.persistance.entity.LedgerGroupMappingDbo;
import com.lazyledger.ledger.infrastructure.persistance.entity.LedgerGroupMappingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SpringDataJpaLedgerGroupMappingRepository extends JpaRepository<LedgerGroupMappingDbo, LedgerGroupMappingId> {

    @Query("SELECT lgm FROM LedgerGroupMappingDbo lgm WHERE lgm.ledgerId = :ledgerId AND lgm.groupId = :groupId")
    LedgerGroupMappingDbo findByLedgerIdAndGroupId(@Param("ledgerId") UUID ledgerId, @Param("groupId") UUID groupId);

    @Query("SELECT lgm FROM LedgerGroupMappingDbo lgm WHERE lgm.ledgerId = :ledgerId")
    List<LedgerGroupMappingDbo> findAllByLedgerId(@Param("ledgerId") UUID ledgerId);

    @Query("SELECT lgm FROM LedgerGroupMappingDbo lgm WHERE lgm.groupId = :groupId")
    List<LedgerGroupMappingDbo> findAllByGroupId(@Param("groupId") UUID groupId);
}