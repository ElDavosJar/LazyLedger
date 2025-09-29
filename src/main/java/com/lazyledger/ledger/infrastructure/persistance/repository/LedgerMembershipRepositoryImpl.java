package com.lazyledger.ledger.infrastructure.persistance.repository;

import com.lazyledger.commons.enums.LedgerUserRole;
import com.lazyledger.commons.identifiers.LedgerId;
import com.lazyledger.commons.identifiers.UserId;
import com.lazyledger.ledger.domain.entities.LedgerMembership;
import com.lazyledger.ledger.domain.repositories.LedgerMembershipRepository;
import com.lazyledger.ledger.infrastructure.persistance.entity.LedgerMembershipDbo;
import com.lazyledger.ledger.infrastructure.persistance.postgres.SpringDataJpaLedgerMembershipRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class LedgerMembershipRepositoryImpl implements LedgerMembershipRepository {

    private final SpringDataJpaLedgerMembershipRepository jpaRepository;

    public LedgerMembershipRepositoryImpl(SpringDataJpaLedgerMembershipRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public LedgerMembership save(LedgerMembership membership) {
        LedgerMembershipDbo dbo = toDbo(membership);
        LedgerMembershipDbo saved = jpaRepository.save(dbo);
        return toDomain(saved);
    }

    @Override
    public void delete(LedgerMembership membership) {
        LedgerMembershipDbo dbo = toDbo(membership);
        jpaRepository.delete(dbo);
    }

    @Override
    public Optional<LedgerMembership> findByUserIdAndLedgerId(UserId userId, LedgerId ledgerId) {
        LedgerMembershipDbo dbo = jpaRepository.findByUserIdAndLedgerId(userId.value(), ledgerId.value());
        return Optional.ofNullable(dbo).map(this::toDomain);
    }

    @Override
    public List<LedgerMembership> findAllByLedgerId(LedgerId ledgerId) {
        return jpaRepository.findAllByLedgerId(ledgerId.value()).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<LedgerMembership> findAllByUserId(UserId userId) {
        return jpaRepository.findAllByUserId(userId.value()).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    private LedgerMembershipDbo toDbo(LedgerMembership membership) {
        return new LedgerMembershipDbo(
            membership.getUserId().value(),
            membership.getLedgerId().value(),
            membership.getRole().getValue()
        );
    }

    private LedgerMembership toDomain(LedgerMembershipDbo dbo) {
        UserId userId = UserId.of(dbo.getUserId());
        LedgerId ledgerId = LedgerId.of(dbo.getLedgerId());
        LedgerUserRole role = LedgerUserRole.fromString(dbo.getRole());
        return LedgerMembership.rehydrate(userId, ledgerId, role);
    }
}