package com.lazyledger.ledger.application;

import com.lazyledger.ledger.domain.repositories.LedgerGroupMappingRepository;
import com.lazyledger.ledger.domain.repositories.LedgerGroupRepository;
import com.lazyledger.ledger.domain.repositories.LedgerMembershipRepository;
import com.lazyledger.ledger.domain.repositories.LedgerRepository;
import com.lazyledger.ledger.domain.repositories.TransactionRepository;
import com.lazyledger.ledger.infrastructure.persistance.repository.LedgerTransactionRepositoryImpl;
import com.lazyledger.ledger.infrastructure.persistance.postgres.SpringDataJpaLedgerTransactionRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class LedgerApplicationConfiguration {

    @Bean
    public LedgerMembershipUseCase ledgerMembershipUseCase(LedgerMembershipRepository membershipRepository) {
        return new LedgerMembershipUseCase(membershipRepository);
    }

    @Bean
    public LedgerGroupUseCase ledgerGroupUseCase(LedgerGroupRepository groupRepository) {
        return new LedgerGroupUseCase(groupRepository);
    }

    @Bean
    public LedgerGroupMappingUseCase ledgerGroupMappingUseCase(LedgerGroupMappingRepository mappingRepository) {
        return new LedgerGroupMappingUseCase(mappingRepository);
    }

    @Bean
    public TransactionRepository transactionRepository(SpringDataJpaLedgerTransactionRepository jpaRepository) {
        return new LedgerTransactionRepositoryImpl(jpaRepository);
    }

    @Bean
    public ManageLedgerTransactionsUseCase manageLedgerTransactionsUseCase(TransactionRepository transactionRepository,
                                                                          LedgerRepository ledgerRepository) {
        return new ManageLedgerTransactionsUseCase(transactionRepository, ledgerRepository);
    }

    @Bean
    public CreateLedgerUseCase createLedgerUseCase(LedgerRepository ledgerRepository,
                                                    LedgerMembershipUseCase membershipUseCase,
                                                    LedgerGroupUseCase groupUseCase,
                                                    LedgerGroupMappingUseCase mappingUseCase) {
        return new CreateLedgerUseCase(ledgerRepository, membershipUseCase, groupUseCase, mappingUseCase);
    }
}