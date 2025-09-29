package com.lazyledger.ledger.domain.entities.transaction.domain;

import com.lazyledger.commons.identifiers.LedgerId;
import com.lazyledger.commons.identifiers.TransactionId;
import com.lazyledger.commons.enums.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Transaction Domain Tests")
class TransactionTest {

    @Test
    @DisplayName("Should create transaction with valid data")
    void shouldCreateTransactionWithValidData() {
        // Given
        TransactionId id = TransactionId.of(UUID.randomUUID());
        LedgerId ledgerId = LedgerId.of(UUID.randomUUID());
        Amount amount = Amount.of(BigDecimal.valueOf(100.50), "USD");
        Description description = Description.of("Test transaction");
        Category category = Category.FOOD;
        TransactionDate transactionDate = TransactionDate.of(LocalDate.now());

        // When
        Transaction transaction = Transaction.of(id, ledgerId, amount, description, category, transactionDate);

        // Then
        assertNotNull(transaction);
        assertEquals(id, transaction.getId());
        assertEquals(ledgerId, transaction.getLedgerId());
        assertEquals(amount, transaction.getAmount());
        assertEquals(description, transaction.getDescription());
        assertEquals(category, transaction.getCategory());
        assertEquals(transactionDate, transaction.getTransactionDate());
        assertNotNull(transaction.getCreatedAt());
    }

    @Test
    @DisplayName("Should create transaction without description")
    void shouldCreateTransactionWithoutDescription() {
        // Given
        TransactionId id = TransactionId.of(UUID.randomUUID());
        LedgerId ledgerId = LedgerId.of(UUID.randomUUID());
        Amount amount = Amount.of(BigDecimal.valueOf(50.00), "EUR");
        Category category = Category.TRANSPORT;
        TransactionDate transactionDate = TransactionDate.of(LocalDate.now());

        // When
        Transaction transaction = Transaction.of(id, ledgerId, amount, null, category, transactionDate);

        // Then
        assertNotNull(transaction);
        assertNull(transaction.getDescription());
    }

    @Test
    @DisplayName("Should rehydrate transaction from persistence")
    void shouldRehydrateTransactionFromPersistence() {
        // Given
        TransactionId id = TransactionId.of(UUID.randomUUID());
        LedgerId ledgerId = LedgerId.of(UUID.randomUUID());
        Amount amount = Amount.of(BigDecimal.valueOf(75.25), "GBP");
        Description description = Description.of("Rehydrated transaction");
        Category category = Category.ENTERTAINMENT;
        TransactionDate transactionDate = TransactionDate.of(LocalDate.now());
        Instant createdAt = Instant.now().minusSeconds(3600);

        // When
        Transaction transaction = Transaction.rehydrate(id, ledgerId, amount, description, category, createdAt, transactionDate);

        // Then
        assertNotNull(transaction);
        assertEquals(id, transaction.getId());
        assertEquals(ledgerId, transaction.getLedgerId());
        assertEquals(amount, transaction.getAmount());
        assertEquals(description, transaction.getDescription());
        assertEquals(category, transaction.getCategory());
        assertEquals(createdAt, transaction.getCreatedAt());
        assertEquals(transactionDate, transaction.getTransactionDate());
    }

    @Test
    @DisplayName("Should throw exception when id is null")
    void shouldThrowExceptionWhenIdIsNull() {
        // Given
        LedgerId ledgerId = LedgerId.of(UUID.randomUUID());
        Amount amount = Amount.of(BigDecimal.valueOf(100.00), "USD");
        Description description = Description.of("Test");
        Category category = Category.FOOD;
        TransactionDate transactionDate = TransactionDate.of(LocalDate.now());

        // When & Then
        assertThrows(NullPointerException.class, () ->
            Transaction.of(null, ledgerId, amount, description, category, transactionDate));
    }

    @Test
    @DisplayName("Should throw exception when ledgerId is null")
    void shouldThrowExceptionWhenLedgerIdIsNull() {
        // Given
        TransactionId id = TransactionId.of(UUID.randomUUID());
        Amount amount = Amount.of(BigDecimal.valueOf(100.00), "USD");
        Description description = Description.of("Test");
        Category category = Category.FOOD;
        TransactionDate transactionDate = TransactionDate.of(LocalDate.now());

        // When & Then
        assertThrows(NullPointerException.class, () ->
            Transaction.of(id, null, amount, description, category, transactionDate));
    }

    @Test
    @DisplayName("Should throw exception when amount is null")
    void shouldThrowExceptionWhenAmountIsNull() {
        // Given
        TransactionId id = TransactionId.of(UUID.randomUUID());
        LedgerId ledgerId = LedgerId.of(UUID.randomUUID());
        Description description = Description.of("Test");
        Category category = Category.FOOD;
        TransactionDate transactionDate = TransactionDate.of(LocalDate.now());

        // When & Then
        assertThrows(NullPointerException.class, () ->
            Transaction.of(id, ledgerId, null, description, category, transactionDate));
    }

    @Test
    @DisplayName("Should throw exception when category is null")
    void shouldThrowExceptionWhenCategoryIsNull() {
        // Given
        TransactionId id = TransactionId.of(UUID.randomUUID());
        LedgerId ledgerId = LedgerId.of(UUID.randomUUID());
        Amount amount = Amount.of(BigDecimal.valueOf(100.00), "USD");
        Description description = Description.of("Test");
        TransactionDate transactionDate = TransactionDate.of(LocalDate.now());

        // When & Then
        assertThrows(NullPointerException.class, () ->
            Transaction.of(id, ledgerId, amount, description, null, transactionDate));
    }

    @Test
    @DisplayName("Should throw exception when transactionDate is null")
    void shouldThrowExceptionWhenTransactionDateIsNull() {
        // Given
        TransactionId id = TransactionId.of(UUID.randomUUID());
        LedgerId ledgerId = LedgerId.of(UUID.randomUUID());
        Amount amount = Amount.of(BigDecimal.valueOf(100.00), "USD");
        Description description = Description.of("Test");
        Category category = Category.FOOD;

        // When & Then
        assertThrows(NullPointerException.class, () ->
            Transaction.of(id, ledgerId, amount, description, category, null));
    }

    @Test
    @DisplayName("Should return correct string representation")
    void shouldReturnCorrectStringRepresentation() {
        // Given
        TransactionId id = TransactionId.of(UUID.randomUUID());
        LedgerId ledgerId = LedgerId.of(UUID.randomUUID());
        Amount amount = Amount.of(BigDecimal.valueOf(123.45), "USD");
        Description description = Description.of("Test transaction");
        Category category = Category.FOOD;
        TransactionDate transactionDate = TransactionDate.of(LocalDate.of(2023, 10, 15));

        Transaction transaction = Transaction.of(id, ledgerId, amount, description, category, transactionDate);

        // When
        String result = transaction.toString();

        // Then
        assertTrue(result.startsWith("Transaction{\n"));
        assertTrue(result.contains("id=" + id));
        assertTrue(result.contains("ledgerId=" + ledgerId));
        assertTrue(result.contains("amount=" + amount));
        assertTrue(result.contains("description=" + description));
        assertTrue(result.contains("category=" + category));
        assertTrue(result.contains("transactionDate=" + transactionDate));
        assertTrue(result.endsWith("\n}"));
    }

    @Test
    @DisplayName("Should be equal when ids are the same")
    void shouldBeEqualWhenIdsAreTheSame() {
        // Given
        TransactionId id = TransactionId.of(UUID.randomUUID());
        LedgerId ledgerId = LedgerId.of(UUID.randomUUID());
        Amount amount = Amount.of(BigDecimal.valueOf(100.00), "USD");
        Description description = Description.of("Test");
        Category category = Category.FOOD;
        TransactionDate transactionDate = TransactionDate.of(LocalDate.now());

        Transaction transaction1 = Transaction.of(id, ledgerId, amount, description, category, transactionDate);
        Transaction transaction2 = Transaction.of(id, LedgerId.of(UUID.randomUUID()), amount, description, category, transactionDate);

        // When & Then
        assertEquals(transaction1, transaction2);
        assertEquals(transaction1.hashCode(), transaction2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when ids are different")
    void shouldNotBeEqualWhenIdsAreDifferent() {
        // Given
        TransactionId id1 = TransactionId.of(UUID.randomUUID());
        TransactionId id2 = TransactionId.of(UUID.randomUUID());
        LedgerId ledgerId = LedgerId.of(UUID.randomUUID());
        Amount amount = Amount.of(BigDecimal.valueOf(100.00), "USD");
        Description description = Description.of("Test");
        Category category = Category.FOOD;
        TransactionDate transactionDate = TransactionDate.of(LocalDate.now());

        Transaction transaction1 = Transaction.of(id1, ledgerId, amount, description, category, transactionDate);
        Transaction transaction2 = Transaction.of(id2, ledgerId, amount, description, category, transactionDate);

        // When & Then
        assertNotEquals(transaction1, transaction2);
        assertNotEquals(transaction1.hashCode(), transaction2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal to null or different type")
    void shouldNotBeEqualToNullOrDifferentType() {
        // Given
        TransactionId id = TransactionId.of(UUID.randomUUID());
        LedgerId ledgerId = LedgerId.of(UUID.randomUUID());
        Amount amount = Amount.of(BigDecimal.valueOf(100.00), "USD");
        Description description = Description.of("Test");
        Category category = Category.FOOD;
        TransactionDate transactionDate = TransactionDate.of(LocalDate.now());

        Transaction transaction = Transaction.of(id, ledgerId, amount, description, category, transactionDate);

        // When & Then
        assertNotEquals(null, transaction);
        assertNotEquals("some string", transaction);
    }

    @Test
    @DisplayName("Should handle negative amounts correctly")
    void shouldHandleNegativeAmountsCorrectly() {
        // Given
        TransactionId id = TransactionId.of(UUID.randomUUID());
        LedgerId ledgerId = LedgerId.of(UUID.randomUUID());
        Amount amount = Amount.of(BigDecimal.valueOf(-50.00), "USD");
        Description description = Description.of("Refund transaction");
        Category category = Category.FOOD;
        TransactionDate transactionDate = TransactionDate.of(LocalDate.now());

        // When
        Transaction transaction = Transaction.of(id, ledgerId, amount, description, category, transactionDate);

        // Then
        assertNotNull(transaction);
        assertEquals(BigDecimal.valueOf(-50.00), transaction.getAmount().value());
    }

    @Test
    @DisplayName("Should handle zero amount")
    void shouldHandleZeroAmount() {
        // Given
        TransactionId id = TransactionId.of(UUID.randomUUID());
        LedgerId ledgerId = LedgerId.of(UUID.randomUUID());
        Amount amount = Amount.of(BigDecimal.ZERO, "USD");
        Description description = Description.of("Zero transaction");
        Category category = Category.FOOD;
        TransactionDate transactionDate = TransactionDate.of(LocalDate.now());

        // When
        Transaction transaction = Transaction.of(id, ledgerId, amount, description, category, transactionDate);

        // Then
        assertNotNull(transaction);
        assertEquals(BigDecimal.ZERO, transaction.getAmount().value());
    }
}