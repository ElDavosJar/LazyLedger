package com.lazyledger.ledger.domain.repositories;

import com.lazyledger.commons.identifiers.LedgerGroupId;
import com.lazyledger.commons.identifiers.LedgerId;
import com.lazyledger.ledger.domain.entities.LedgerGroupMapping;

import java.util.List;
import java.util.Optional;

public interface LedgerGroupMappingRepository {

    LedgerGroupMapping save(LedgerGroupMapping mapping);

    Optional<LedgerGroupMapping> findByLedgerIdAndGroupId(LedgerId ledgerId, LedgerGroupId groupId);

    List<LedgerGroupMapping> findAllByLedgerId(LedgerId ledgerId);

    List<LedgerGroupMapping> findAllByGroupId(LedgerGroupId groupId);

    void delete(LedgerGroupMapping mapping);
}