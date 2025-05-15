package com.tinhuynhtrung.BankManager.Repository;

import com.tinhuynhtrung.BankManager.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.deletedAt IS NULL AND t.account.id = :accountId")
    List<Transaction> findActiveByAccountId(Long accountId);

    @Query("SELECT t FROM Transaction t WHERE t.id = :id AND t.deletedAt IS NULL")
    Optional<Transaction> findActiveById(Long id);

    @Modifying
    @Query("UPDATE Transaction t SET t.deletedAt = :deletedAt WHERE t.id = :id")
    void softDelete(Long id, LocalDateTime deletedAt);
}
