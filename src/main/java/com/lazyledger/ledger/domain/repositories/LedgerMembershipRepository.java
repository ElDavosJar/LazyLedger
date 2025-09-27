package com.lazyledger.ledger.domain.repositories;

import com.lazyledger.ledger.domain.entities.LedgerMembership;


import com.lazyledger.commons.identifiers.UserId;
import com.lazyledger.commons.identifiers.LedgerId;
import java.util.List;
import java.util.Optional;

public interface LedgerMembershipRepository {
    LedgerMembership save(LedgerMembership user);
    void delete(LedgerMembership user);
    Optional<LedgerMembership> findByUserIdAndLedgerId(UserId userId, LedgerId ledgerId);
    //list all users by ledgerId
    List<LedgerMembership> findAllByLedgerId(LedgerId ledgerId);
    //list all ledgers by userId
    List<LedgerMembership> findAllByUserId(UserId userId);
}