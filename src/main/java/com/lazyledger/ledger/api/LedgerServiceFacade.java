package com.lazyledger.ledger.api;

import com.lazyledger.commons.exceptions.InvalidInputException;
import com.lazyledger.commons.identifiers.UserId;
import com.lazyledger.ledger.api.dto.LedgerDto;
import com.lazyledger.ledger.application.CreateLedgerUseCase;
import com.lazyledger.ledger.domain.entities.Ledger;
import com.lazyledger.ledger.domain.entities.vo.LedgerName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class LedgerServiceFacade {

    private final CreateLedgerUseCase createLedgerUseCase;

    @Autowired
    public LedgerServiceFacade(CreateLedgerUseCase createLedgerUseCase) {
        this.createLedgerUseCase = createLedgerUseCase;
    }

    public LedgerDto createLedger(String name, UUID userId, String groupId, String groupName) {
        try {
            UserId createdBy = UserId.of(userId);
            LedgerName ledgerName = LedgerName.of(name);
            // Parse groupId if provided
            Optional<UUID> groupIdOpt = Optional.empty();
            if (groupId != null && !groupId.trim().isEmpty()) {
                groupIdOpt = Optional.of(UUID.fromString(groupId));
            }
            Ledger ledger = createLedgerUseCase.createLedger(createdBy, ledgerName, groupIdOpt, groupName);
            return LedgerMapper.toLedgerDto(ledger);
        } catch (IllegalArgumentException e) {
            throw new InvalidInputException("Invalid input data: " + e.getMessage(), e);
        }
    }
}