package com.lazyledger.ledger.infrastructure.persistance.repository;

import com.lazyledger.commons.identifiers.LedgerGroupId;
import com.lazyledger.commons.identifiers.LedgerId;
import com.lazyledger.ledger.domain.entities.LedgerGroupMapping;
import com.lazyledger.ledger.domain.repositories.LedgerGroupMappingRepository;
import com.lazyledger.ledger.infrastructure.persistance.entity.LedgerGroupMappingDbo;
import com.lazyledger.ledger.infrastructure.persistance.postgres.SpringDataJpaLedgerGroupMappingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class LedgerGroupMappingRepositoryImpl implements LedgerGroupMappingRepository {

    private final SpringDataJpaLedgerGroupMappingRepository jpaRepository;

    public LedgerGroupMappingRepositoryImpl(SpringDataJpaLedgerGroupMappingRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public LedgerGroupMapping save(LedgerGroupMapping mapping) {
        LedgerGroupMappingDbo dbo = toDbo(mapping);
        LedgerGroupMappingDbo saved = jpaRepository.save(dbo);
        return toDomain(saved);
    }

    @Override
    public Optional<LedgerGroupMapping> findByLedgerIdAndGroupId(LedgerId ledgerId, LedgerGroupId groupId) {
        LedgerGroupMappingDbo dbo = jpaRepository.findByLedgerIdAndGroupId(ledgerId.value(), groupId.value());
        return Optional.ofNullable(dbo).map(this::toDomain);
    }

    @Override
    public List<LedgerGroupMapping> findAllByLedgerId(LedgerId ledgerId) {
        return jpaRepository.findAllByLedgerId(ledgerId.value()).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<LedgerGroupMapping> findAllByGroupId(LedgerGroupId groupId) {
        return jpaRepository.findAllByGroupId(groupId.value()).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void delete(LedgerGroupMapping mapping) {
        LedgerGroupMappingDbo dbo = toDbo(mapping);
        jpaRepository.delete(dbo);
    }

    private LedgerGroupMappingDbo toDbo(LedgerGroupMapping mapping) {
        return new LedgerGroupMappingDbo(
            mapping.getLedgerId().value(),
            mapping.getGroupId().value()
        );
    }

    private LedgerGroupMapping toDomain(LedgerGroupMappingDbo dbo) {
        LedgerId ledgerId = LedgerId.of(dbo.getLedgerId());
        LedgerGroupId groupId = LedgerGroupId.of(dbo.getGroupId());
        return LedgerGroupMapping.rehydrate(ledgerId, groupId);
    }
}