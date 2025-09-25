package com.lazyledger.transaction.application;

import com.lazyledger.transaction.domain.repository.TransactionRepository;
import com.lazyledger.transaction.infrastructure.persistance.repository.UserTransactionCounterRepository;
import com.lazyledger.transcription.transcribers.TranscriptionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionServiceConfiguration {

    @Bean
    public TransactionService transactionService(TransactionRepository transactionRepository, TranscriptionService transcriptionService, UserTransactionCounterRepository counterRepository) {
        return new TransactionService(transactionRepository, transcriptionService, counterRepository);
    }
}