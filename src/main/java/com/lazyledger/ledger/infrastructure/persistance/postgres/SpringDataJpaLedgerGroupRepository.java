package com.lazyledger.ledger.infrastructure.persistance.postgres;

import com.lazyledger.ledger.infrastructure.persistance.entity.LedgerGroupDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataJpaLedgerGroupRepository extends JpaRepository<LedgerGroupDbo, UUID> {

    @Query("SELECT lg FROM LedgerGroupDbo lg WHERE lg.ownerId = :ownerId")
    List<LedgerGroupDbo> findByOwnerId(@Param("ownerId") UUID ownerId);

    @Query("SELECT lg FROM LedgerGroupDbo lg WHERE lg.ownerId = :ownerId AND lg.name = :name")
    Optional<LedgerGroupDbo> findByOwnerIdAndName(@Param("ownerId") UUID ownerId, @Param("name") String name);
}