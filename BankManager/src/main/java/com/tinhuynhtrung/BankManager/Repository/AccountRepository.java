package com.tinhuynhtrung.BankManager.Repository;

import com.tinhuynhtrung.BankManager.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT a FROM Account a WHERE a.deletedAt IS NULL AND a.customer.id = :customerId")
    List<Account> findActiveByCustomerId(Long customerId);

    @Query("SELECT a FROM Account a WHERE a.id = :id AND a.deletedAt IS NULL")
    Optional<Account> findActiveById(Long id);

    @Modifying
    @Query("UPDATE Account a SET a.deletedAt = :deletedAt WHERE a.id = :id")
    void softDelete(Long id, LocalDateTime deletedAt);

    @Query("SELECT a FROM Account a WHERE a.accountNumber LIKE %:keyword% AND a.deletedAt IS NULL")
    List<Account> searchByAccountNumber(@Param("keyword") String keyword);

    //boolean existsByAccountNumber(String accountNumber); kiểm tra cả các account đã bị xóa

    boolean existsByAccountNumberAndDeletedAtIsNull(String accountNumber);
}
