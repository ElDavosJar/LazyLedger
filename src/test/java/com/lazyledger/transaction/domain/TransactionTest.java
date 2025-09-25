package com.lazyledger.transaction.domain;

import com.lazyledger.commons.TransactionId;
import com.lazyledger.commons.UserId;
import com.lazyledger.commons.enums.Category;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    private final TransactionId id = TransactionId.of(UUID.randomUUID());
    private final UserId userId = UserId.of(UUID.randomUUID());
    private final Amount amount = Amount.of(BigDecimal.valueOf(100), "USD");
    private final Description description = new Description("Test transaction");
    private final Category category = Category.FOOD;
    private final TransactionDate transactionDate = new TransactionDate(LocalDate.now());

    @Test
    void testOfCreatesTransactionWithCurrentTime() {
        Transaction transaction = Transaction.of(id, userId, 1L, amount, description, category, transactionDate);

        assertEquals(id, transaction.getId());
        assertEquals(userId, transaction.getUserId());
        assertEquals(amount, transaction.getAmount());
        assertEquals(description, transaction.getDescription());
        assertEquals(category, transaction.getCategory());
        assertEquals(transactionDate, transaction.getTransactionDate());
        assertNotNull(transaction.getCreatedAt());
        assertTrue(transaction.getCreatedAt().isBefore(Instant.now().plusSeconds(1)));
    }

    @Test
    void testRehydrateCreatesTransactionWithGivenTime() {
        Instant createdAt = Instant.parse("2023-01-01T00:00:00Z");
        Transaction transaction = Transaction.rehydrate(id, userId, 1L, amount, description, category, createdAt, transactionDate);

        assertEquals(id, transaction.getId());
        assertEquals(userId, transaction.getUserId());
        assertEquals(amount, transaction.getAmount());
        assertEquals(description, transaction.getDescription());
        assertEquals(category, transaction.getCategory());
        assertEquals(createdAt, transaction.getCreatedAt());
        assertEquals(transactionDate, transaction.getTransactionDate());
    }

    @Test
    void testOfWithNullDescription() {
        Transaction transaction = Transaction.of(id, userId, 1L, amount, null, category, transactionDate);

        assertNull(transaction.getDescription());
    }

    @Test
    void testGetters() {
        Instant createdAt = Instant.now();
        Transaction transaction = Transaction.rehydrate(id, userId, 1L, amount, description, category, createdAt, transactionDate);

        assertEquals(id, transaction.getId());
        assertEquals(userId, transaction.getUserId());
        assertEquals(amount, transaction.getAmount());
        assertEquals(description, transaction.getDescription());
        assertEquals(category, transaction.getCategory());
        assertEquals(createdAt, transaction.getCreatedAt());
        assertEquals(transactionDate, transaction.getTransactionDate());
    }

    @Test
    void testToString() {
        Transaction transaction = Transaction.of(id, userId, 1L, amount, description, category, transactionDate);
        String toString = transaction.toString();

        assertTrue(toString.contains("Transaction{"));
        assertTrue(toString.contains("id=" + id));
        assertTrue(toString.contains("userId=" + userId));
        assertTrue(toString.contains("amount=" + amount));
        assertTrue(toString.contains("description=" + description));
        assertTrue(toString.contains("category=" + category));
        assertTrue(toString.contains("createdAt="));
        assertTrue(toString.contains("transactionDate=" + transactionDate));
    }

    @Test
    void testEquals() {
        Transaction transaction1 = Transaction.of(id, userId, 1L, amount, description, category, transactionDate);
        Transaction transaction2 = Transaction.rehydrate(id, userId, 1L, amount, description, category, Instant.now(), transactionDate);
        TransactionId differentId = TransactionId.of(UUID.randomUUID());
        Transaction transaction3 = Transaction.of(differentId, userId, 1L, amount, description, category, transactionDate);

        assertEquals(transaction1, transaction2); // same id
        assertNotEquals(transaction1, transaction3); // different id
        assertNotEquals(transaction1, null);
        assertNotEquals(transaction1, new Object());
    }

    @Test
    void testHashCode() {
        Transaction transaction1 = Transaction.of(id, userId, 1L, amount, description, category, transactionDate);
        Transaction transaction2 = Transaction.rehydrate(id, userId, 1L, amount, description, category, Instant.now(), transactionDate);

        assertEquals(transaction1.hashCode(), transaction2.hashCode());
    }

    @Test
    void testOfThrowsOnNullId() {
        assertThrows(NullPointerException.class, () ->
            Transaction.of(null, userId, 1L, amount, description, category, transactionDate));
    }

    @Test
    void testOfThrowsOnNullUserId() {
        assertThrows(NullPointerException.class, () ->
            Transaction.of(id, null, 1L, amount, description, category, transactionDate));
    }

    @Test
    void testOfThrowsOnNullAmount() {
        assertThrows(NullPointerException.class, () ->
            Transaction.of(id, userId, 1L, null, description, category, transactionDate));
    }

    @Test
    void testOfThrowsOnNullCategory() {
        assertThrows(NullPointerException.class, () ->
            Transaction.of(id, userId, 1L, amount, description, null, transactionDate));
    }

    @Test
    void testOfThrowsOnNullTransactionDate() {
        assertThrows(NullPointerException.class, () ->
            Transaction.of(id, userId, 1L, amount, description, category, null));
    }

    @Test
    void testRehydrateThrowsOnNullId() {
        assertThrows(NullPointerException.class, () ->
            Transaction.rehydrate(null, userId, 1L, amount, description, category, Instant.now(), transactionDate));
    }

    @Test
    void testRehydrateThrowsOnNullUserId() {
        assertThrows(NullPointerException.class, () ->
            Transaction.rehydrate(id, null, 1L, amount, description, category, Instant.now(), transactionDate));
    }

    @Test
    void testRehydrateThrowsOnNullAmount() {
        assertThrows(NullPointerException.class, () ->
            Transaction.rehydrate(id, userId, 1L, null, description, category, Instant.now(), transactionDate));
    }

    @Test
    void testRehydrateThrowsOnNullCategory() {
        assertThrows(NullPointerException.class, () ->
            Transaction.rehydrate(id, userId, 1L, amount, description, null, Instant.now(), transactionDate));
    }

    @Test
    void testRehydrateThrowsOnNullCreatedAt() {
        assertThrows(NullPointerException.class, () ->
            Transaction.rehydrate(id, userId, 1L, amount, description, category, null, transactionDate));
    }

    @Test
    void testRehydrateThrowsOnNullTransactionDate() {
        assertThrows(NullPointerException.class, () ->
            Transaction.rehydrate(id, userId, 1L, amount, description, category, Instant.now(), null));
    }
}