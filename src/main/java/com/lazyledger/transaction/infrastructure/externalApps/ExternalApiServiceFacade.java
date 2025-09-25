package com.lazyledger.transaction.infrastructure.externalApps;

import com.lazyledger.transaction.api.dto.TransactionDto;
import com.lazyledger.transaction.application.TransactionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExternalApiServiceFacade {

    private final TransactionService transactionService;

    public ExternalApiServiceFacade(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public List<TransactionDto> processFile(byte[] fileBytes, String mediaType) {
        return transactionService.processFile(fileBytes, mediaType);
    }
}