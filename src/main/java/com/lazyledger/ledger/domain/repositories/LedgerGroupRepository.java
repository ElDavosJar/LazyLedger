package com.lazyledger.ledger.domain.repositories;

import com.lazyledger.commons.identifiers.LedgerGroupId;
import com.lazyledger.commons.UserId;
import com.lazyledger.ledger.domain.entities.LedgerGroup;
import java.util.List;
import java.util.Optional;

public interface LedgerGroupRepository {

    LedgerGroup save(LedgerGroup ledgerGroup);

    Optional<LedgerGroup> findById(LedgerGroupId id);

    List<LedgerGroup> findAll();

    List<LedgerGroup> findByOwnerId(UserId ownerId);

    void delete(LedgerGroupId id);
}