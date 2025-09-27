package com.lazyledger.ledger.domain.domainServices;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;

import com.lazyledger.commons.identifiers.UserId;
import com.lazyledger.commons.enums.LedgerUserRole;
import com.lazyledger.commons.identifiers.LedgerId;
import com.lazyledger.ledger.domain.entities.Ledger;
import com.lazyledger.ledger.domain.entities.LedgerMembership;
import com.lazyledger.ledger.domain.repositories.LedgerMembershipRepository;

public class LedgerUserService {
    private final LedgerMembershipRepository membershipRepository;
    
    public LedgerUserService(LedgerMembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }
    //invite user to ledger with role
    public LedgerMembership inviteUserToLedger(UserId userId, LedgerId ledgerId){
        LedgerMembership membership = LedgerMembership.create(userId, ledgerId, LedgerUserRole.VIEWER);
        return membershipRepository.save(membership);
    }

    public void removeUserFromLedger(UserId userId, LedgerId ledgerId){
        LedgerMembership membership = membershipRepository.findByUserIdAndLedgerId(userId, ledgerId)
                .orElseThrow(() -> new IllegalArgumentException("User is not a member of the ledger"));
        membershipRepository.delete(membership);
    }

    public LedgerMembership changeUserRole(UserId userId, LedgerId ledgerId, LedgerUserRole newRole){
        LedgerMembership membership = membershipRepository.findByUserIdAndLedgerId(userId, ledgerId)
                .orElseThrow(() -> new IllegalArgumentException("User is not a member of the ledger"));
        LedgerMembership updatedMembership = LedgerMembership.create(userId, ledgerId, newRole);
        return membershipRepository.save(updatedMembership);
    }

    public LedgerMembership inviteAssistant(UserId userId, LedgerId ledgerId){
        LedgerMembership membership = LedgerMembership.create(userId, ledgerId, LedgerUserRole.ASSISTANT);
        return membershipRepository.save(membership);
    }
}