package com.lazyledger.ledger.domain.repositories;

import com.lazyledger.commons.identifiers.LedgerId;
import com.lazyledger.ledger.domain.entities.Ledger;
import java.util.List;
import java.util.Optional;

public interface LedgerRepository {

    Ledger save(Ledger ledger);

    Optional<Ledger> findById(LedgerId id);

    List<Ledger> findAll();

    void delete(LedgerId id);
} 
