package com.lazyledger.ledger.application;

import com.lazyledger.commons.enums.LedgerUserRole;
import com.lazyledger.commons.identifiers.LedgerId;
import com.lazyledger.commons.identifiers.UserId;
import com.lazyledger.ledger.domain.entities.LedgerMembership;
import com.lazyledger.ledger.domain.repositories.LedgerMembershipRepository;

import java.util.List;
import java.util.Optional;

public class LedgerMembershipUseCase {
    private final LedgerMembershipRepository membershipRepository;

    public LedgerMembershipUseCase(LedgerMembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }

    public LedgerMembership createMembership(UserId userId, LedgerId ledgerId, LedgerUserRole role) {
        LedgerMembership membership = LedgerMembership.create(userId, ledgerId, role);
        return membershipRepository.save(membership);
    }

    public Optional<LedgerMembership> findByUserIdAndLedgerId(UserId userId, LedgerId ledgerId) {
        return membershipRepository.findByUserIdAndLedgerId(userId, ledgerId);
    }

    public List<LedgerMembership> findAllByLedgerId(LedgerId ledgerId) {
        return membershipRepository.findAllByLedgerId(ledgerId);
    }

    public List<LedgerMembership> findAllByUserId(UserId userId) {
        return membershipRepository.findAllByUserId(userId);
    }

    public void removeMembership(LedgerMembership membership) {
        membershipRepository.delete(membership);
    }
}