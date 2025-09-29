package com.lazyledger.ledger.application;

import com.lazyledger.commons.identifiers.LedgerGroupId;
import com.lazyledger.commons.identifiers.LedgerId;
import com.lazyledger.ledger.domain.entities.LedgerGroupMapping;
import com.lazyledger.ledger.domain.repositories.LedgerGroupMappingRepository;

import java.util.List;
import java.util.Optional;

public class LedgerGroupMappingUseCase {
    private final LedgerGroupMappingRepository mappingRepository;

    public LedgerGroupMappingUseCase(LedgerGroupMappingRepository mappingRepository) {
        this.mappingRepository = mappingRepository;
    }

    public LedgerGroupMapping createMapping(LedgerId ledgerId, LedgerGroupId groupId) {
        LedgerGroupMapping mapping = LedgerGroupMapping.create(ledgerId, groupId);
        return mappingRepository.save(mapping);
    }

    public Optional<LedgerGroupMapping> findByLedgerIdAndGroupId(LedgerId ledgerId, LedgerGroupId groupId) {
        return mappingRepository.findByLedgerIdAndGroupId(ledgerId, groupId);
    }

    public List<LedgerGroupMapping> findAllByLedgerId(LedgerId ledgerId) {
        return mappingRepository.findAllByLedgerId(ledgerId);
    }

    public List<LedgerGroupMapping> findAllByGroupId(LedgerGroupId groupId) {
        return mappingRepository.findAllByGroupId(groupId);
    }

    public void removeMapping(LedgerGroupMapping mapping) {
        mappingRepository.delete(mapping);
    }
}