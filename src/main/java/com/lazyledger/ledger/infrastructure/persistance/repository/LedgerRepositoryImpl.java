package com.lazyledger.ledger.infrastructure.persistance.repository;

import com.lazyledger.commons.enums.LedgerStatus;
import com.lazyledger.commons.identifiers.LedgerId;
import com.lazyledger.commons.identifiers.UserId;
import com.lazyledger.ledger.domain.entities.Ledger;
import com.lazyledger.ledger.domain.entities.vo.LedgerName;
import com.lazyledger.ledger.domain.repositories.LedgerRepository;
import com.lazyledger.ledger.infrastructure.persistance.entity.LedgerDbo;
import com.lazyledger.ledger.infrastructure.persistance.postgres.SpringDataJpaLedgerRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class LedgerRepositoryImpl implements LedgerRepository {

    private final SpringDataJpaLedgerRepository jpaRepository;

    public LedgerRepositoryImpl(SpringDataJpaLedgerRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Ledger save(Ledger ledger) {
        LedgerDbo dbo = toDbo(ledger);
        LedgerDbo saved = jpaRepository.save(dbo);
        return toDomain(saved);
    }

    @Override
    public Optional<Ledger> findById(LedgerId id) {
        Optional<LedgerDbo> dboOpt = jpaRepository.findById(id.value());
        return dboOpt.map(this::toDomain);
    }

    @Override
    public List<Ledger> findAll() {
        return jpaRepository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void delete(LedgerId id) {
        jpaRepository.deleteById(id.value());
    }

    private LedgerDbo toDbo(Ledger ledger) {
        return new LedgerDbo(
            ledger.getId().value(),
            ledger.getCreatedBy().value(),
            ledger.getName().value(),
            ledger.getStatus().toString(),
            ledger.getCreatedAt()
        );
    }

    private Ledger toDomain(LedgerDbo dbo) {
        LedgerId id = LedgerId.of(dbo.getId());
        UserId createdBy = UserId.of(dbo.getCreatedBy());
        LedgerName name = LedgerName.of(dbo.getName());
        LedgerStatus status = LedgerStatus.fromString(dbo.getStatus());
        return Ledger.rehydrate(id, createdBy, name, status, dbo.getCreatedAt());
    }
}