package com.lazyledger.ledger.domain.repositories;

import com.lazyledger.commons.identifiers.LedgerGroupId;
import com.lazyledger.commons.identifiers.UserId;
import com.lazyledger.ledger.domain.entities.LedgerGroup;
import com.lazyledger.ledger.domain.entities.vo.LedgerName;
import java.util.List;
import java.util.Optional;

public interface LedgerGroupRepository {

    LedgerGroup save(LedgerGroup ledgerGroup);

    Optional<LedgerGroup> findById(LedgerGroupId id);

    List<LedgerGroup> findAll();

    List<LedgerGroup> findByOwnerId(UserId ownerId);

    Optional<LedgerGroup> findByOwnerIdAndName(UserId ownerId, LedgerName name);

    void delete(LedgerGroupId id);
}