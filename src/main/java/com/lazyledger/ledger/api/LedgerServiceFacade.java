package com.lazyledger.ledger.api;

import com.lazyledger.commons.identifiers.UserId;
import com.lazyledger.ledger.api.dto.LedgerDto;
import com.lazyledger.ledger.application.CreateLedgerUseCase;
import com.lazyledger.ledger.domain.entities.Ledger;
import com.lazyledger.ledger.domain.entities.vo.LedgerName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LedgerServiceFacade {

    private final CreateLedgerUseCase createLedgerUseCase;

    @Autowired
    public LedgerServiceFacade(CreateLedgerUseCase createLedgerUseCase) {
        this.createLedgerUseCase = createLedgerUseCase;
    }

    public LedgerDto createLedger(String name, UUID userId, String groupId, String groupName) {
        UserId createdBy = UserId.of(userId);
        LedgerName ledgerName = LedgerName.of(name);
        Ledger ledger = createLedgerUseCase.createLedger(createdBy, ledgerName, groupId, groupName);
        return LedgerMapper.toLedgerDto(ledger);
    }
}