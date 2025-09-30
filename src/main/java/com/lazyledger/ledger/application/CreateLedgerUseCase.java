package com.lazyledger.ledger.application;

import com.lazyledger.commons.enums.LedgerUserRole;
import com.lazyledger.commons.identifiers.LedgerGroupId;
import com.lazyledger.commons.identifiers.LedgerId;
import com.lazyledger.commons.identifiers.UserId;
import com.lazyledger.ledger.domain.entities.Ledger;
import com.lazyledger.ledger.domain.entities.LedgerGroup;
import com.lazyledger.ledger.domain.entities.vo.LedgerName;
import com.lazyledger.ledger.domain.repositories.LedgerRepository;

import java.util.Optional;
import java.util.UUID;

public class CreateLedgerUseCase {
    private final LedgerRepository ledgerRepository;
    private final LedgerMembershipUseCase membershipUseCase;
    private final LedgerGroupUseCase groupUseCase;
    private final LedgerGroupMappingUseCase mappingUseCase;

    public CreateLedgerUseCase(LedgerRepository ledgerRepository, LedgerMembershipUseCase membershipUseCase,
                              LedgerGroupUseCase groupUseCase, LedgerGroupMappingUseCase mappingUseCase) {
        this.ledgerRepository = ledgerRepository;
        this.membershipUseCase = membershipUseCase;
        this.groupUseCase = groupUseCase;
        this.mappingUseCase = mappingUseCase;
    }

    public Ledger createLedger(UserId createdBy, LedgerName name, Optional<UUID> groupIdOpt, String groupName) {
        LedgerId id = LedgerId.of(UUID.randomUUID());
        Ledger ledger = Ledger.create(id, createdBy, name);
        Ledger savedLedger = ledgerRepository.save(ledger);

        // Create the owner membership automatically
        membershipUseCase.createMembership(createdBy, id, LedgerUserRole.OWNER);

        // Handle optional group
        if (groupIdOpt.isPresent()) {
            // Use existing group
            LedgerGroupId existingGroupId = LedgerGroupId.of(groupIdOpt.get());
            mappingUseCase.createMapping(id, existingGroupId);
        } else if (groupName != null && !groupName.trim().isEmpty()) {
            // Check if user already has a group with this name, if not create it
            LedgerName groupLedgerName = LedgerName.of(groupName);
            Optional<LedgerGroup> existingGroup = groupUseCase.findByOwnerIdAndName(createdBy, groupLedgerName);
            LedgerGroupId groupIdToUse;
            if (existingGroup.isPresent()) {
                groupIdToUse = existingGroup.get().getId();
            } else {
                LedgerGroup newGroup = groupUseCase.createGroup(createdBy, groupLedgerName);
                groupIdToUse = newGroup.getId();
            }
            mappingUseCase.createMapping(id, groupIdToUse);
        }

        return savedLedger;
    }
}