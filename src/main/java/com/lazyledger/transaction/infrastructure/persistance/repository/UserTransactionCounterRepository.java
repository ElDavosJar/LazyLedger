package com.lazyledger.transaction.infrastructure.persistance.repository;

import com.lazyledger.transaction.infrastructure.persistance.entity.UserTransactionCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;

public interface UserTransactionCounterRepository extends JpaRepository<UserTransactionCounter, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM UserTransactionCounter c WHERE c.userId = :userId")
    Optional<UserTransactionCounter> findByUserIdForUpdate(@Param("userId") UUID userId);
}