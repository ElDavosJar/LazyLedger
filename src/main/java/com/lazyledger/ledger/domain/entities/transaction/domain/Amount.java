package com.lazyledger.ledger.domain.entities.transaction.domain;

import java.math.BigDecimal;
//amount can be positive or negative for debit and credit
public record Amount(BigDecimal value, String currency) {
    public Amount {
        if (value == null) {
            throw new IllegalArgumentException("Amount value must be non-null");
        }
        if (currency == null || currency.isEmpty()) {
            throw new IllegalArgumentException("Currency must be non-null and non-empty");
        }
    }

    @Override
    public String toString() {
        return value + " " + currency;
    }
    private static boolean isValidCurrency(String currency) {
        if (currency.length() != 3 || !currency.chars().allMatch(Character::isLetter)) {
            return false;
        }
        //Is basic check, can be improved with regex, need to check if currency is valid against notional standards, pending
        return true;
    }
    public static Amount of(BigDecimal value, String currency) {
        if (!isValidCurrency(currency)) {
            throw new IllegalArgumentException("Invalid currency");
        }
        return new Amount(value, currency);
    }

}