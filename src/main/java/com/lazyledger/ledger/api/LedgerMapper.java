package com.lazyledger.ledger.api;

import com.lazyledger.ledger.api.dto.LedgerDto;
import com.lazyledger.ledger.domain.entities.Ledger;

public class LedgerMapper {

    public static LedgerDto toLedgerDto(Ledger ledger) {
        return LedgerDto.from(
            ledger.getId().toString(),
            ledger.getCreatedBy().toString(),
            ledger.getName().value(),
            ledger.getStatus().toString(),
            ledger.getCreatedAt().toString()
        );
    }
}