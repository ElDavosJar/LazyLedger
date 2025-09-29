package com.lazyledger.ledger.infrastructure.persistance.repository;

import com.lazyledger.commons.identifiers.LedgerGroupId;
import com.lazyledger.commons.identifiers.UserId;
import com.lazyledger.ledger.domain.entities.LedgerGroup;
import com.lazyledger.ledger.domain.entities.vo.LedgerName;
import com.lazyledger.ledger.domain.repositories.LedgerGroupRepository;
import com.lazyledger.ledger.infrastructure.persistance.entity.LedgerGroupDbo;
import com.lazyledger.ledger.infrastructure.persistance.postgres.SpringDataJpaLedgerGroupRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class LedgerGroupRepositoryImpl implements LedgerGroupRepository {

    private final SpringDataJpaLedgerGroupRepository jpaRepository;

    public LedgerGroupRepositoryImpl(SpringDataJpaLedgerGroupRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public LedgerGroup save(LedgerGroup ledgerGroup) {
        var dbo = toDbo(ledgerGroup);
        var saved = jpaRepository.save(dbo);
        return toDomain(saved);
    }

    @Override
    public Optional<LedgerGroup> findById(LedgerGroupId id) {
        return jpaRepository.findById(id.value()).map(this::toDomain);
    }

    @Override
    public List<LedgerGroup> findAll() {
        return jpaRepository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<LedgerGroup> findByOwnerId(UserId ownerId) {
        return jpaRepository.findByOwnerId(ownerId.value()).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<LedgerGroup> findByOwnerIdAndName(UserId ownerId, LedgerName name) {
        return jpaRepository.findByOwnerIdAndName(ownerId.value(), name.value()).map(this::toDomain);
    }

    @Override
    public void delete(LedgerGroupId id) {
        jpaRepository.deleteById(id.value());
    }

    private LedgerGroupDbo toDbo(LedgerGroup ledgerGroup) {
        return new LedgerGroupDbo(
            ledgerGroup.getId().value(),
            ledgerGroup.getOwnerId().value(),
            ledgerGroup.getName().value()
        );
    }

    private LedgerGroup toDomain(LedgerGroupDbo dbo) {
        return LedgerGroup.rehydrate(
            LedgerGroupId.of(dbo.getId()),
            UserId.of(dbo.getOwnerId()),
            new LedgerName(dbo.getName())
        );
    }
}