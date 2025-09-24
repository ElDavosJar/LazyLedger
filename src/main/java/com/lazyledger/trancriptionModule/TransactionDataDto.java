package com.lazyledger.trancriptionModule;

import java.math.BigDecimal;

public record TransactionDataDto(
    BigDecimal amount,
    String currency,
    String description,
    String category,
    String transactionDate
) {}