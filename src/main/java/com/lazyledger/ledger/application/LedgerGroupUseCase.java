package com.lazyledger.ledger.application;

import com.lazyledger.commons.identifiers.UserId;
import com.lazyledger.commons.identifiers.LedgerGroupId;
import com.lazyledger.ledger.domain.entities.LedgerGroup;
import com.lazyledger.ledger.domain.entities.vo.LedgerName;
import com.lazyledger.ledger.domain.repositories.LedgerGroupRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LedgerGroupUseCase {
    private final LedgerGroupRepository groupRepository;

    public LedgerGroupUseCase(LedgerGroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public LedgerGroup createGroup(UserId ownerId, LedgerName name) {
        // Check if user already has a group with this name
        Optional<LedgerGroup> existingGroup = groupRepository.findByOwnerIdAndName(ownerId, name);
        if (existingGroup.isPresent()) {
            throw new IllegalArgumentException("User already has a ledger group with name: " + name.value());
        }

        LedgerGroupId id = LedgerGroupId.of(UUID.randomUUID());
        LedgerGroup group = LedgerGroup.create(id, ownerId, name);
        return groupRepository.save(group);
    }

    public Optional<LedgerGroup> findById(LedgerGroupId id) {
        return groupRepository.findById(id);
    }

    public List<LedgerGroup> findAll() {
        return groupRepository.findAll();
    }

    public List<LedgerGroup> findByOwnerId(UserId ownerId) {
        return groupRepository.findByOwnerId(ownerId);
    }

    public Optional<LedgerGroup> findByOwnerIdAndName(UserId ownerId, LedgerName name) {
        return groupRepository.findByOwnerIdAndName(ownerId, name);
    }

    public void deleteGroup(LedgerGroupId id) {
        groupRepository.delete(id);
    }
}